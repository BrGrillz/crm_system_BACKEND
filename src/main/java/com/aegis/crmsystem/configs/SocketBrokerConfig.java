package com.aegis.crmsystem.configs;

import com.aegis.crmsystem.Auth;
import com.aegis.crmsystem.models.User;
import com.aegis.crmsystem.repositories.UserRepository;
import com.aegis.crmsystem.security.jwt.JwtTokenProvider;
import com.aegis.crmsystem.security.jwt.JwtUser;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.security.Principal;
import java.time.LocalDateTime;

@Slf4j
@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
class SocketBrokerConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    @Qualifier("clientOutboundChannel")
    private MessageChannel clientOutboundChannel;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/task", "/usr", "/status", "/user");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/aegis_crm_system").setAllowedOrigins("*").withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {

            @SneakyThrows
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                assert accessor != null;
                final Principal userPrincipal = accessor.getUser();

                if(userPrincipal != null){
                    final Authentication auth = (Authentication) accessor.getUser();

                    if(auth != null){
                        final JwtUser jwtUser = (JwtUser) auth.getPrincipal();
                        Auth.jwtUser = jwtUser;
                        Auth.user = jwtUser.getUser();

                        User user = jwtUser.getUser();
                        user.setLastVisit(LocalDateTime.now());
                        
                        userRepository.save(user);
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                }


                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    try {
                        String token = accessor.getFirstNativeHeader("Authorization");

                        log.info("+++++___________________________________TOKEN______________________________+++++ {}", token);

                        if (token != null && jwtTokenProvider.validateAccessToken(token)) {
                            Authentication auth = jwtTokenProvider.getAuthentication(token);

                            log.info("+++++___________________________________AUTH______________________________+++++ {}", auth);

                            if (auth != null) {
                                accessor.setUser(auth);
                                SecurityContextHolder.getContext().setAuthentication(auth);

                                final Authentication res = SecurityContextHolder.getContext().getAuthentication();
                                final JwtUser jwtUser = (JwtUser) res.getPrincipal();
                                Auth.jwtUser = jwtUser;
                                Auth.user = jwtUser.getUser();
                                log.info("+++++___________________________________USER______________________________+++++ {}", jwtUser);
                            }
                        } else {
                            sendStompError("401", accessor.getSessionId());
                        }
                    } catch (RuntimeException e) {

                        sendStompError("401", accessor.getSessionId());
                    }
                }
                return message;
            }
        });
    }

    public void sendStompError(String errorMessage, String sessionId) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.create(StompCommand.ERROR);
        headerAccessor.setMessage(errorMessage);
        headerAccessor.setSessionId(sessionId);
        this.clientOutboundChannel.send(MessageBuilder.createMessage(new byte[0], headerAccessor.getMessageHeaders()));
    }

//    @Override
//    public void configureClientInboundChannel(ChannelRegistration registration) {
//        log.info("+++++_________________________________________________________________+++++");
//        registration.interceptors(new ChannelInterceptor() {
//
//            @Override
//            public Message<?> preSend(Message<?> message, MessageChannel channel) {
//
//                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
//
//                log.info("in override " + accessor.getCommand());
//
//                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
//
////                    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
////                    String name = auth.getName(); //get logged in username
////                    System.out.println("Authenticated User : " + name);
//
//                    String authToken = accessor.getFirstNativeHeader("Authorization");
//
//                    log.info("Header auth token: " + authToken);
//
////                    Principal principal = authenticationService.getUserFromToken(authToken);
//
////                    if (Objects.isNull(principal))
////                        return null;
////
////                    accessor.setUser(principal);
//                } else if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
//                    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//                    if (Objects.nonNull(authentication))
//                        log.info("Disconnected Auth : " + authentication.getName());
//                    else
//                        log.info("Disconnected Sess : " + accessor.getSessionId());
//                }
//                return message;
//            }
//
//            @Override
//            public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
//                StompHeaderAccessor sha = StompHeaderAccessor.wrap(message);
//
//                // ignore non-STOMP messages like heartbeat messages
//                if (sha.getCommand() == null) {
//                    log.warn("postSend null command");
//                    return;
//                }
//
//                String sessionId = sha.getSessionId();
//
//                switch (sha.getCommand()) {
//                    case CONNECT:
//                        log.info("STOMP Connect [sessionId: " + sessionId + "]");
//                        break;
//                    case CONNECTED:
//                        log.info("STOMP Connected [sessionId: " + sessionId + "]");
//                        break;
//                    case DISCONNECT:
//                        log.info("STOMP Disconnect [sessionId: " + sessionId + "]");
//                        break;
//                    default:
//                        break;
//
//                }
//            }
//        });
//
//    }
}