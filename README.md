# Pinview

[![version](https://img.shields.io/badge/version-1.1-green.svg)](https://semver.org)

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
	        implementation 'com.github.Deepak-811:pinview:1.1'
	}
  ```
  Sync the gradle and that's it
  
  ### Features : 
 * Flawless focus change to the consecutive pin box when the text is entered/deleted.
 * When the user taps on the Pinview, the first empty box available is focused automatically (when the cursor is hidden).
 * Customisations are available for pin box sizes, background(drawables, selectors), inputType etc.
  
  ## Usage
  
  **XML**
  ```xml
 <com.deepak.library.PinView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:pinSize="6"
        app:pinHint="*"
        app:boxSpacing="8dp"
        app:boxWidth="40dp"
        app:boxHeight="40dp"
        app:boxBackground="@drawable/sample_background"/>
 ```
 This can be referenced in the java class by the **findViewById** method.
 
 ##### Available xml attributes and explanations : 

```app:boxBackground``` : Sets the pin box's background, accepts a drawable <br />
```app:boxWidth``` and ```app:boxHeight``` : Sets the width and height of the pinbox. <br />
```app:pinSize``` : number of pin boxes to be displayed.<br />
```app:pinHint``` : Pin box hint. <br />
 
 **How to get the Values?**
 
 **Method 1**
 ```java
 PinView pinview = findViewById(R.id.pinview);
 pinview.getValues();
 ```
 
 **Method 2**
 ```java
  pinview.setPinCompleteListener(new PinView.PinCompleteListener() {
            @Override
            public void onCompletePin(String result, boolean isCompleted) {
                Toast.makeText(MainActivity.this, "Completed: " + isCompleted + "\nValue: " + result,
                        Toast.LENGTH_SHORT).show();
            }
        });
 ```
 ## Default Values
 
 ```app:boxBackground``` : Round <br />
```app:boxWidth``` and ```app:boxHeight``` :  width = 40dp and height= 40dp <br />
```app:pinSize``` : 4<br />
```app:pinHint``` :* <br />
 

 
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
