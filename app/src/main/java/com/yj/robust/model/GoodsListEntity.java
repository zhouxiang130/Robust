package com.yj.robust.model;

import java.util.List;

/**
 * Created by Suo on 2018/3/21.
 */

public class GoodsListEntity {
    private String code;
    private String msg;
    private GoodsListData data;

    public final String HTTP_OK ="200";
    public final String HTTP_OK_ ="400";


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public GoodsListData getData() {
        return data;
    }

    public void setData(GoodsListData data) {
        this.data = data;
    }

    public class GoodsListData{


        private String code;
        private String pageNumber;
        private String detailNumMonth;
        private String shopNumber;
        private String receipt;
        private String serviceStartime;
        private String shopId;
        private String shopImg;
        private String shopName;
        private String shopType;
        private String shopTypeName;

        public String getShopNumber() {
            return shopNumber;
        }

        public void setShopNumber(String shopNumber) {
            this.shopNumber = shopNumber;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getPageNumber() {
            return pageNumber;
        }

        public void setPageNumber(String pageNumber) {
            this.pageNumber = pageNumber;
        }

        public String getDetailNumMonth() {
            return detailNumMonth;
        }

        public void setDetailNumMonth(String detailNumMonth) {
            this.detailNumMonth = detailNumMonth;
        }

        public String  getReceipt() {
            return receipt;
        }

        public void setReceipt(String receipt) {
            this.receipt = receipt;
        }

        public String getServiceStartime() {
            return serviceStartime;
        }

        public void setServiceStartime(String serviceStartime) {
            this.serviceStartime = serviceStartime;
        }

        public String getShopId() {
            return shopId;
        }

        public void setShopId(String shopId) {
            this.shopId = shopId;
        }

        public String getShopImg() {
            return shopImg;
        }

        public void setShopImg(String shopImg) {
            this.shopImg = shopImg;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public String getShopType() {
            return shopType;
        }

        public void setShopType(String shopType) {
            this.shopType = shopType;
        }

        public String getShopTypeName() {
            return shopTypeName;
        }

        public void setShopTypeName(String shopTypeName) {
            this.shopTypeName = shopTypeName;
        }

        private List<GoodsListItem> proJsonArray;

        public List<GoodsListItem> getProJsonArray() {
            return proJsonArray;
        }

        public void setProJsonArray(List<GoodsListItem> proJsonArray) {
            this.proJsonArray = proJsonArray;
        }

        public class GoodsListItem{
            private String commentNum;
            private String proId;
            private String proName;
            private String productOrginal;
            private String proImg;
            private String shopId;
            private String shopName;
            private String classifyId;



            private String productHot;
            private String productAbstract;
            private String productTimelimit;
            private String productCurrent;
            private String consumerBackmoney;
            private String sproductId;

            public String getShopId() {
                return shopId;
            }

            public void setShopId(String shopId) {
                this.shopId = shopId;
            }

            public String getShopName() {
                return shopName;
            }

            public void setShopName(String shopName) {
                this.shopName = shopName;
            }

            public String getSproductId() {
                return sproductId;
            }

            public void setSproductId(String sproductId) {
                this.sproductId = sproductId;
            }

            public String getConsumerBackmoney() {
                return consumerBackmoney;
            }

            public void setConsumerBackmoney(String consumerBackmoney) {
                this.consumerBackmoney = consumerBackmoney;
            }

            public String getClassifyId() {
                return classifyId;
            }

            public void setClassifyId(String classifyId) {
                this.classifyId = classifyId;
            }

            public String getCommentNum() {
                return commentNum;
            }

            public void setCommentNum(String commentNum) {
                this.commentNum = commentNum;
            }

            public String getProductAbstract() {
                return productAbstract;
            }

            public void setProductAbstract(String productAbstract) {
                this.productAbstract = productAbstract;
            }

            public String getProductCurrent() {
                return productCurrent;
            }

            public void setProductCurrent(String productCurrent) {
                this.productCurrent = productCurrent;
            }

            public String getProductHot() {
                return productHot;
            }

            public void setProductHot(String productHot) {
                this.productHot = productHot;
            }

            public String getProductOrginal() {
                return productOrginal;
            }

            public void setProductOrginal(String productOrginal) {
                this.productOrginal = productOrginal;
            }

            public String getProductTimelimit() {
                return productTimelimit;
            }

            public void setProductTimelimit(String productTimelimit) {
                this.productTimelimit = productTimelimit;
            }

            public String getProId() {
                return proId;
            }

            public void setProId(String proId) {
                this.proId = proId;
            }

            public String getProImg() {
                return proImg;
            }

            public void setProImg(String proImg) {
                this.proImg = proImg;
            }

            public String getProName() {
                return proName;
            }

            public void setProName(String proName) {
                this.proName = proName;
            }
        }
    }

}