package com.example.ovo.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class JasaListResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<Jasa> data;

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<Jasa> getData() {
        return data;
    }

    public static class Jasa {
        @SerializedName("id_jasa")
        private String idJasa;

        @SerializedName("blntahun")
        private String blnTahun;

        @SerializedName("jumlah")
        private String jumlah;

        @SerializedName("status")
        private String status;

        @SerializedName("bulan")
        private String bulan;

        @SerializedName("tahun")
        private String tahun;

        // Getter dan Setter untuk semua atribut

        public String getIdJasa() {
            return idJasa;
        }

        public void setIdJasa(String idJasa) {
            this.idJasa = idJasa;
        }

        public String getBlnTahun() {
            return blnTahun;
        }

        public void setBlnTahun(String blnTahun) {
            this.blnTahun = blnTahun;
        }

        public String getJumlah() {
            return jumlah;
        }

        public void setJumlah(String jumlah) {
            this.jumlah = jumlah;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getBulan() {
            return bulan;
        }

        public void setBulan(String bulan) {
            this.bulan = bulan;
        }

        public String getTahun() {
            return tahun;
        }

        public void setTahun(String tahun) {
            this.tahun = tahun;
        }
    }
}
