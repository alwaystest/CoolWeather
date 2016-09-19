package com.software.eric.coolweather.mvc.choosearea;

import com.software.eric.coolweather.entity.Address;
import com.software.eric.coolweather.entity.County;

import java.util.List;

/**
 * Created by Mzz on 2016/7/8.
 */
public interface ChooseAreaContract {
    /**
     * Created by Mzz on 2016/2/15.
     */
    interface IChooseAreaModel {
        void saveSelectedCounty(County county);
        boolean checkCountySelected();
        void queryProvinces(ChooseAreaModelImpl.OnLoadListListener listener);
        void queryCities(Address selectedProvince, ChooseAreaModelImpl.OnLoadListListener listener);
        void queryCounties(Address selectedCity, ChooseAreaModelImpl.OnLoadListListener listener);
        String getString(int id);
    }

    /**
     * Created by Mzz on 2016/2/15.
     */
    interface IChooseAreaPresenter {
        void saveSelectedCounty(County county);
        boolean checkCountySelected();
        void queryProvinces();
        void queryCities(Address selectedProvince);
        void queryCounties(Address selectedCity);
        void checkIfGoToWeatherActivity(boolean isFromWeatherActivity);
        void onListItemClicked(Address selectedAddress);
    }

    /**
     * Created by Mzz on 2016/2/15.
     */
    interface IChooseAreaView {
        void showProgressDialog();

        void closeProgressDialog();

        void setList(List<? extends Address> addressList);

        void setTitle(String title);

        void setCurrentLevel(int level);

        void setSelectedAddress(Address selectedAddress);

        int getCurrentLevel();

        void goToWeatherActivity();

        void initView();
    }
}
