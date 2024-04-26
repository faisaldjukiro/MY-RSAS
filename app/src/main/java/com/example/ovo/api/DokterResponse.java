package com.example.ovo.api;

public class DokterResponse {
    private String status;
    private String message;
    private Data[] data;

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

    public Data[] getData() {
        return data;
    }

    public void setData(Data[] data) {
        this.data = data;
    }


    public class Data {
        private String kasus;
        private DataKasus[] data_kasus;

        public String getKasus() {
            return kasus;
        }

        public void setKasus(String kasus) {
            this.kasus = kasus;
        }

        public DataKasus[] getData_kasus() {
            return data_kasus;
        }

        public void setData_kasus(DataKasus[] data_kasus) {
            this.data_kasus = data_kasus;
        }
    }

    public class DataKasus {
        private String pasien;
        private DataPasien[] data_pasien;

        public String getPasien() {
            return pasien;
        }

        public void setPasien(String pasien) {
            this.pasien = pasien;
        }

        public DataPasien[] getData_pasien() {
            return data_pasien;
        }

        public void setData_pasien(DataPasien[] data_pasien) {
            this.data_pasien = data_pasien;
        }


        public class DataPasien {
            private String tindakan;
            private String jumlah;
            private String klem;

            public String getTindakan() {
                return tindakan;
            }

            public void setTindakan(String tindakan) {
                this.tindakan = tindakan;
            }

            public String getJumlah() {
                return jumlah;
            }

            public void setJumlah(String jumlah) {
                this.jumlah = jumlah;
            }

            public String getKlem() {
                return klem;
            }

            public void setKlem(String klem) {
                this.klem = klem;
            }
        }
    }
}
