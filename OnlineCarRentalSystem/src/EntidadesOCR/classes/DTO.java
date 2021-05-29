/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EntidadesOCR.classes;

import java.time.LocalDate;

/**
 *
 * @author Administrator
 */
public class DTO <T> {
    private T obj;
    private LocalDate time;

    public DTO(T obj) {
        this.obj = obj;
        this.time=LocalDate.now();
    }

    public DTO() {
        this.time=LocalDate.now();
    }

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }

    public LocalDate getTime() {
        return time;
    }

    public void setTime(LocalDate time) {
        this.time = time;
    }
}
