package org.walter.orm.repository.demo1;

import lombok.*;

@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Demo1Domain extends BaseDomain{
    private String code;
    private String name;

    public Demo1Domain(Long id, String code, String name){
        super(id);
        this.setCode(code);
        this.setName(name);
    }
}
