package com.example.ovo.api;

import com.google.gson.annotations.SerializedName;

public class CheckKdPegawaiResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("Data")
    private PegawaiData data;

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

    public PegawaiData getData() {
        return data;
    }

    public void setData(PegawaiData data) {
        this.data = data;
    }

    public class PegawaiData {
        @SerializedName("kd_peg")
        private String kdPegawai;

        @SerializedName("nama")
        private String namaLengkap;

        public String getKdPegawai() {
            return kdPegawai;
        }

        public void setKdPegawai(String kdPegawai) {
            this.kdPegawai = kdPegawai;
        }

        public String getNamaLengkap() {
            return namaLengkap;
        }

        public void setNamaLengkap(String namaLengkap) {
            this.namaLengkap = namaLengkap;
        }
    }
}
