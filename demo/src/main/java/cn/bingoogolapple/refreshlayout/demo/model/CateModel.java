package cn.bingoogolapple.refreshlayout.demo.model;

/**
 * @PACKAGE cn.bingoogolapple.refreshlayout.demo.model
 * @DESCRIPTION: TODO
 * @AUTHOR dongen_wang
 * @DATE 15/8/17 11:12
 * @VERSION V1.0
 */
public class CateModel
{

    /**
     * order : 0
     * name : 钓鱼名人
     * cate_id : 7601
     * type : 3
     * short_name : 名人
     * is_final : 2
     */
    private String order;
    private String name;
    private String cate_id;
    private String type;
    private String short_name;
    private String is_final;

    public void setOrder(String order) {
        this.order = order;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCate_id(String cate_id) {
        this.cate_id = cate_id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public void setIs_final(String is_final) {
        this.is_final = is_final;
    }

    public String getOrder() {
        return order;
    }

    public String getName() {
        return name;
    }

    public String getCate_id() {
        return cate_id;
    }

    public String getType() {
        return type;
    }

    public String getShort_name() {
        return short_name;
    }

    public String getIs_final() {
        return is_final;
    }
}
