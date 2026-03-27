### Android Main

This directory contains all the android specific code. look out for 
functions which have **actual** in their declaration. These always have a 
corresponding **expected** declaration in [commonMain](../commonMain). If
all went well these declarations should be in a similar file path as
[androidMain](.). One such example is [getPlatform()](./kotlin/karldrabek/goobaapp/goobaapp/Platform.android.kt)
which will return the correct platform when called in [commonMain](../commonMain).

### Local Storage

We implement Android local storage in [AndroidSessionStorage.kt](./kotlin/karldrabek/goobaapp/goobaapp/localStorage/AndroidSessionStorage.kt) which is passed into the state 
for the application regardless of whether it runs on android or IOS.

### Time Picker

There is also an unused time picker in [TimePicker.kt](./kotlin/karldrabek/goobaapp/goobaapp/ui/utils/TimePicker.kt) which fully
works on android and IOS, this being the android version, but is not used.
