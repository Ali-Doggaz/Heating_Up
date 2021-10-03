/*

package com.example.Balayage.Util;

import com.google.code.geocoder.Geocoder;
import org.apache.tomcat.jni.Address;

public class getCityLatitudeAndLongitude {

    public boolean getLatitudeAndLongitudeFromGoogleMapForAddress(String searchedAddress){

        Geocoder coder = new Geocoder(IPlant.iPlantActivity);
        List<Address> address;
        try
        {
            address = coder.getFromLocationName(searchedAddress,5);
            if (address == null) {
                Log.d(TAG, "############Address not correct #########");
            }
            Address location = address.get(0);

            Log.d(TAG, "Address Latitude : "+ location.getLatitude();+ "Address Longitude : "+ location.getLongitude());
            return true;

        }
        catch(Exception e)
        {
            Log.d(TAG, "MY_ERROR : ############Address Not Found");
            return false;
        }
    }
}*/
