package ru.workflow.staff;

import com.google.gson.annotations.SerializedName;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 *  Элемент организационной структуры
 *  @author IShabalin
 */
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class Staff {

    /** Идентификатор */
    @SerializedName("uid")
    @XmlAttribute(name="id")
    protected int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
