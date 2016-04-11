package com.yinnonbar.securedwallet;

/**
 * Created by Yinnon Bratspiess on 07/04/2016.
 */
public class WalletItem {
    private String key;
    private String value;
    public WalletItem() {
    }
    public WalletItem(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
