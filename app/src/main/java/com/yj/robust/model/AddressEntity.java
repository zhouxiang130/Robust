package com.yj.robust.model;

import java.util.List;

/**
 * Created by Suo on 2018/3/22.
 */

public class AddressEntity {
    private String code;
    private String msg;
    private AddressData data;
    public final String HTTP_OK ="200";


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public AddressData getData() {
        return data;
    }

    public void setData(AddressData data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public class AddressData {
        private List<AddressList> userAddressMaps;

        public List<AddressList> getUserAddressMaps() {
            return userAddressMaps;
        }

        public void setUserAddressMaps(List<AddressList> userAddressMaps) {
            this.userAddressMaps = userAddressMaps;
        }

        public class AddressList{
            private String addressDefault;
            private String receiverTel;
            private String receiverName;
            private String addressId;
            private String addressAreaDetail;
            private String addressArea;
            private String type;//1 置灰  2  不置灰

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getAddressArea() {
                return addressArea;
            }

            public void setAddressArea(String addressArea) {
                this.addressArea = addressArea;
            }

            public String getAddressAreaDetail() {
                return addressAreaDetail;
            }

            public void setAddressAreaDetail(String addressAreaDetail) {
                this.addressAreaDetail = addressAreaDetail;
            }

            public String getAddressDefault() {
                return addressDefault;
            }

            public void setAddressDefault(String addressDefault) {
                this.addressDefault = addressDefault;
            }

            public String getAddressId() {
                return addressId;
            }

            public void setAddressId(String addressId) {
                this.addressId = addressId;
            }

            public String getReceiverName() {
                return receiverName;
            }

            public void setReceiverName(String receiverName) {
                this.receiverName = receiverName;
            }

            public String getReceiverTel() {
                return receiverTel;
            }

            public void setReceiverTel(String receiverTel) {
                this.receiverTel = receiverTel;
            }
        }
    }
}