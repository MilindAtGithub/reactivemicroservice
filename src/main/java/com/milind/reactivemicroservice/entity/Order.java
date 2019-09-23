package com.milind.reactivemicroservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Document(collection = "ORDER")
public class Order {

    @Id
    private Integer orderId;

    private List<String> items;

    private Date createDate = new Date();

    public Order() {
    }

    public Order(Integer orderId, @NotBlank List<String> items) {
        this.orderId = orderId;
        this.items = items;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    public Date getCreateDate() {
        return createDate;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", items=" + items==null?"0":items.size() +
                ", createDate=" + createDate +
                '}';
    }
}
