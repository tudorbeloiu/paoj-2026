package com.pao.proiect.licitatii.model.products;

import java.util.List;
import com.pao.proiect.licitatii.exception.InvalidDigitalFormat;

public class DigitalProduct extends Product{

    private static final List<String> availableFormats = List.of("MP3", "MP4", "PDF", "XML", "JSON", "ZIP", "RAR", "GZ", "APK", "PNG", "JPG", "ISO");

    private String format;
    private String licenseKey;

    public DigitalProduct(String name, String description, double price, Category category, String format, String license){
        super(name, description, price, category);

        if(!availableFormats.contains(format)){
            throw new InvalidDigitalFormat(format);
        }
        this.format = format;
        this.licenseKey = license;
    }

    @Override
    public String getReceiveInfo(){
        return "[Format]: " + format + " | [License Key]: " + licenseKey;
    }
    @Override
    public String toString(){
        return super.toString() + " | " + getReceiveInfo();
    }

    public String getFormat(){
        return format;
    }
    public String getLicenseKey(){
        return licenseKey;
    }

    public void setFormat(String chosenFormat){
        if(!availableFormats.contains(chosenFormat)){
            throw new InvalidDigitalFormat(chosenFormat);
        }
        this.format = chosenFormat;
    }
    public void setLicenseKey(String licenseKey){
        this.licenseKey = licenseKey;
    }



}
