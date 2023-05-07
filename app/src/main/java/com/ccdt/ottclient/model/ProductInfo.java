package com.ccdt.ottclient.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
{
    "message": {
        "status": "1",
        "msg": "马上购买该产品",
        "serverTime": "20150624171503"
    },
    "data": [
        {
            "productName": "按次",
            "productId": "acdb",
            "businessModel": "num",
            "productPrice": 2,
            "productState": "1",
            "remark": "123456",
            "businessModelName": "按次",
            "id": 36,
            "createTime": "2015-05-14 13:49:13",
            "updateTime": "2015-05-14 13:49:49"
        },
        {
            "productName": "包月",
            "productId": "by",
            "businessModel": "mon",
            "productPrice": 10,
            "productState": "1",
            "remark": "",
            "businessModelName": "包月",
            "id": 37,
            "createTime": "2015-05-14 13:49:39",
            "updateTime": "2015-05-14 13:49:39"
        }
    ]
}
*/

/**
 * @Package com.ccdt.stb.movies.data.model
 * @ClassName: ProductInfo
 * @Description: 资产对应的产品信息
 * @author hezb
 * @date 2015年6月24日 下午5:17:34
 */

public class ProductInfo extends BaseObject implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private DataMessage message;
    private List<Product> data = new ArrayList<>();

    @Override
    public void parserJSON(JSONObject jsonObject) throws Exception {
        if (jsonObject != null) {
            JSONObject jsonMsg = jsonObject.optJSONObject("message");
            if (jsonMsg != null) {
                message = new DataMessage();
                message.parserJSON(jsonMsg);
            }

            JSONArray jsonArr = jsonObject.optJSONArray("data");
            if (jsonArr != null) {
                for (int i = 0; i < jsonArr.length(); i++) {
                    JSONObject jsonPro = jsonArr.optJSONObject(i);
                    if (jsonPro != null) {
                        Product pro = new Product();
                        pro.parserJSON(jsonPro);
                        getData().add(pro);
                    }
                }
            }

        }
    }




    public DataMessage getMessage() {
        return message;
    }

    public void setMessage(DataMessage message) {
        this.message = message;
    }

    public List<Product> getData() {
        return data;
    }



    @Override
    public void parserMap(Map<String, String> map) throws Exception {

    }

    public class Product extends BaseObject implements Serializable {

        private static final long serialVersionUID = 1L;
        
        private String productName;
        private String productId;
        private String businessModel;
        private int productPrice;
        private String productState;
        private String remark;
        private String businessModelName;
        private String id;
        private String createTime;
        private String updateTime;
        public Product(){}

        @Override
        public void parserJSON(JSONObject jsonObject) throws Exception {
            if (jsonObject != null) {
                setProductName(jsonObject.optString("productName"));
                setProductId(jsonObject.optString("productId"));
                setBusinessModel(jsonObject.optString("businessModel"));
                setProductPrice(jsonObject.optInt("productPrice"));
                setProductState(jsonObject.optString("productState"));
                setRemark(jsonObject.optString("remark"));
                setBusinessModelName(jsonObject.optString("businessModelName"));
                setId(jsonObject.optString("id"));
                setCreateTime(jsonObject.optString("createTime"));
                setUpdateTime(jsonObject.optString("updateTime"));
            }
        }


        
        public String getProductName() {
            return productName;
        }
        public void setProductName(String productName) {
            this.productName = productName;
        }
        public String getProductId() {
            return productId;
        }
        public void setProductId(String productId) {
            this.productId = productId;
        }
        public String getBusinessModel() {
            return businessModel;
        }
        public void setBusinessModel(String businessModel) {
            this.businessModel = businessModel;
        }
        public int getProductPrice() {
            return productPrice;
        }
        public void setProductPrice(int productPrice) {
            this.productPrice = productPrice;
        }
        public String getProductState() {
            return productState;
        }
        public void setProductState(String productState) {
            this.productState = productState;
        }
        public String getRemark() {
            return remark;
        }
        public void setRemark(String remark) {
            this.remark = remark;
        }
        public String getBusinessModelName() {
            return businessModelName;
        }
        public void setBusinessModelName(String businessModelName) {
            this.businessModelName = businessModelName;
        }
        public String getId() {
            return id;
        }
        public void setId(String id) {
            this.id = id;
        }
        public String getCreateTime() {
            return createTime;
        }
        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
        public String getUpdateTime() {
            return updateTime;
        }
        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }



        @Override
        public void parserMap(Map<String, String> map) throws Exception {

        }

        @Override
        public String toString() {
            return "Product{" +
                    "productName='" + productName + '\'' +
                    ", productId='" + productId + '\'' +
                    ", businessModel='" + businessModel + '\'' +
                    ", productPrice=" + productPrice +
                    ", productState='" + productState + '\'' +
                    ", remark='" + remark + '\'' +
                    ", businessModelName='" + businessModelName + '\'' +
                    ", id='" + id + '\'' +
                    ", createTime='" + createTime + '\'' +
                    ", updateTime='" + updateTime + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ProductInfo{" +
                "message=" + message +
                ", data=" + data +
                '}';
    }
}
