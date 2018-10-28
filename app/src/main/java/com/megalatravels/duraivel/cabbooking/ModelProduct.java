package com.megalatravels.duraivel.cabbooking;

public class ModelProduct {
    private int image;
    private String head;
    private String desc;
    private String comp;
    private String cost;
    private String amount;
    private String cg;

    public ModelProduct(int image, String head, String desc, String comp, String cost, String amount, String cg)
    {
        this.image = image;
        this.head = head;
        this.desc = desc;
        this.comp=comp;
        this.cost=cost;
        this.amount=amount;
        this.cg=cg;
    }


    public String getCg() { return cg; }

    public void setCg(String cg) { this.cg = cg; }

    public String getComp() { return comp; }

    public void setComp(String comp) { this.comp = comp; }

    public String getCost() { return cost; }

    public void setCost(String cost) { this.cost = cost; }

    public String getAmount() { return amount; }

    public void setAmount(String amount) { this.amount = amount; }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
