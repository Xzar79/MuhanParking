package com.example.muhanparking.model.response;

import java.util.List;
import com.example.muhanparking.model.request.IotInfoRequest;

public class IotInfoResponse<T> {

        private int status;
        private String message;
        private T data;

        public IotInfoResponse(int status, String message, T data) {
            this.status = status;
            this.message = message;
            this.data = data;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }
    }
