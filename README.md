# pinview

[![version](https://img.shields.io/badge/version-1.0-green.svg)](https://semver.org)

## Installation

Add it in your root build.gradle at the end of repositories:

```java
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
 Add the dependency
 ```java
  dependencies {
	        implementation 'com.github.Deepak-811:pinview:1.0'
	}
  ```
  Sync the gradle and that's it
  
  ## Usage
  
  **XML**
  ```html
  <com.deepak.library.PinView
        android:id="@+id/pinview"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        pinview:password="true"
        pinview:split="-"
        pinview:pinHint="*"
        pinview:numberCharacters="1"
        pinview:deleteOnClick="false"
        pinview:keyboardMandatory="false"
        pinview:nativePinBox="false"
        pinview:colorSplit="@android:color/darker_gray"
        pinview:colorTextPinBox="@android:color/black"
        pinview:colorTextTitles="@android:color/black"
        />
 ```
 This can be referenced in the java class by the findViewById method.
 
 **How to get the Values?**
 
 **Method 1**
 ```java
 PinView pinview = findViewById(R.id.pinview);
 pinview.getPinResults();
 ```
 
 **Method 2**
 ```java
  pinview.setOnCompleteListener(new PinView.OnCompleteListener() {
            @Override
            public void onComplete(boolean completed, String pinResults) {
                Toast.makeText(MainActivity.this, "Completed: pinview_prog--" + completed + "\nValue: " + pinResults,
                        Toast.LENGTH_SHORT).show();
            }
        });
 ```
 
 ## Setting Pinview programmatically
 
 **Step-1**
 
 ```html
  <com.deepak.library.PinView
        android:id="@+id/pinview_prog"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
      />
 ```
 
 **Step-2**
 ```java
 PinView pinview_prog = findViewById(R.id.pinview_prog);
 ```
 
 **Step-3**
 ```java
  PinViewSettings pinViewSettings = new PinViewSettings.Builder()
                .withMaskPassword(true)
                .withDeleteOnClick(true)
                .withKeyboardMandatory(false)
                .withSplit(null)
                .withHint("#")
                .withNumberPinBoxes(5)
                .withNativePinBox(false)
                .build();
                
  pinview_prog.setSettings(pinViewSettings);
  
 ```
 
 ## License
MIT License
```java
Copyright (c) 2020 PinView

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
