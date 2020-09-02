package com.nikhil.EnPass.listeners;

import com.nikhil.EnPass.enums.ImagePickerEnum;

@FunctionalInterface
public interface IImagePickerLister {
    void onOptionSelected(ImagePickerEnum imagePickerEnum);
}
