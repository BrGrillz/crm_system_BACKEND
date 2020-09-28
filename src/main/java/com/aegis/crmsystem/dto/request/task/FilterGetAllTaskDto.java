package com.aegis.crmsystem.dto.request.task;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
public class FilterGetAllTaskDto {
    private Boolean author;
    private Boolean responsible;
    private Boolean observers;
    private Boolean deleted;
    private Long status;
    private String search;

    public Boolean isEmpty(){
        return this.author == null &&
                this.responsible == null &&
                this.observers == null &&
                this.deleted == null &&
                this.status == null &&
                this.search == null;
    }
}
