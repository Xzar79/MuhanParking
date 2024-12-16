package com.example.muhanparking.model.response;

import com.example.muhanparking.model.request.IotInfoRequest;
import java.util.List;

public class IotInfoResponse {
    private List<IotInfoRequest> iotInfo;

    public List<IotInfoRequest> getIotInfo() {
        return iotInfo;
    }

    public void setIotInfo(List<IotInfoRequest> iotInfo) {
        this.iotInfo = iotInfo;
    }
}