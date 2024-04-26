package com.example.ovo.api;

public class JasaSatuResponse {
    private String status;
    private String message;
    private DataModel data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataModel getData() {
        return data;
    }

    public void setData(DataModel data) {
        this.data = data;
    }

    public class DataModel {
        private String jumlah;
        private String blnthn;
        public String getJumlah() {
            return jumlah;
        }

        public void setJumlah(String jumlah) {
            this.jumlah = jumlah;
        }

        public String getBlnthn() {
            return blnthn;
        }

        public void setBlnthn(String blnthn) {
            this.blnthn = blnthn;
        }


    }
}

