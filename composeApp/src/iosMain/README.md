### IOS Main

This directory contains all the IOS specific code. look out for 
functions which have **actual** in their declaration. These always have a 
corresponding **expected** declaration in [commonMain](../commonMain). If
all went well these declarations should be in a similar file path as
[iosMain](.). One such example is [getPlatform()](./kotlin/karldrabek/goobaapp/goobaapp/Platform.ios.kt)
which will return the correct platform when called in [commonMain](../commonMain).

### Local Storage

We implement IOS local storage in [IosSessionStorage.kt](./kotlin/karldrabek/goobaapp/goobaapp/localStorage/IosSessionStorage.kt) which is passed into the state 
for the application regardless of whether it runs on android or IOS.

### Time Picker

There is also an unused time picker in [TimePicker.kt](./kotlin/karldrabek/goobaapp/goobaapp/ui/utils/TimePicker.kt) which fully
works on android and IOS, this being the android version, but is not used.
