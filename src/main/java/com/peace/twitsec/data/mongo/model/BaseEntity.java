package com.peace.twitsec.data.mongo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseEntity {
	
	protected BaseEntity(){
	}
	
    @Id
    private String id;

    public boolean isEqual(BaseEntity request){
    	return this.id.equalsIgnoreCase(request.getId());
    }
}
