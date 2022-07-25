package com.mobiroller.core.coreui.models;

import com.mobiroller.core.coreui.helpers.LocalizationHelper;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ealtaca on 09.03.2017.
 */

public class CategoryModel implements Serializable{

    private String aveCatalogCategoryID;
    private String categoryTitle;
    private String categorySubTitle;
    private ImageModel categoryImage;
    private ImageModel catagoryThumbImage;
    private ArrayList<CategoryItemModel> categoryItems;

    public String getAveCatalogCategoryID() {
        return aveCatalogCategoryID;
    }

    public void setAveCatalogCategoryID(String aveCatalogCategoryID) {
        this.aveCatalogCategoryID = aveCatalogCategoryID;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public String getCategorySubTitle() {
        return categorySubTitle;
    }

    public void setCategorySubTitle(String categorySubTitle) {
        this.categorySubTitle = categorySubTitle;
    }

    public ImageModel getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(ImageModel categoryImage) {
        this.categoryImage = categoryImage;
    }

    public ImageModel getCategoryThumbImage() {
        return catagoryThumbImage;
    }

    public void setCategoryThumbImage(ImageModel categoryThumbImage) {
        this.catagoryThumbImage = categoryThumbImage;
    }

    public ArrayList<CategoryItemModel> getCategoryItems() {
        return categoryItems;
    }

    public void setCategoryItems(ArrayList<CategoryItemModel> categoryItems) {
        this.categoryItems = categoryItems;
    }



    public void localizeCategoryItemModels(LocalizationHelper localizationHelper)
    {
        if(categoryItems!=null)
        {
            for (CategoryItemModel model :
                    categoryItems) {
                model.setItemTitle(localizationHelper.getLocalizedTitle(model.getItemTitle()));
                model.setItemSubTitle(localizationHelper.getLocalizedTitle(model.getItemSubTitle()));
                model.setItemDescription(localizationHelper.getLocalizedTitle(model.getItemDescription()));
            }
        }
    }
}
