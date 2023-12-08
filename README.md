<p align="center">
  <img width=300 src="https://i.ibb.co/S0jbyTT/round-logo-no-text.png">
</p>
<p align="center">
  <img width=500 src="https://i.ibb.co/VxPMPvT/text-with-background.png">
</p>
<p align="center">
  <img src="https://img.shields.io/badge/version-1.9.10-%237F52FF?style=flat-square&logo=kotlin">
  <img src="https://img.shields.io/badge/bom_version-2023.03.00-%234285F4?style=flat-square&logo=jetpack%20compose">
  <img src="https://img.shields.io/badge/version-1.2.0--alpha11-%23757575?style=flat-square&logo=material%20design">
  <img src="https://img.shields.io/badge/Coil-2.4.0-%233B6BB4?style=flat-square">
  <img src="https://img.shields.io/badge/bom_version-32.3.1-%23FFCA28?style=flat-square&logo=firebase">
  <img src="https://img.shields.io/badge/version-Hedgehog_%7C_2023.1.1-%233DDC84?style=flat-square&logo=android%20studio">
  <img src="https://img.shields.io/badge/minSdk-24-%233DDC84?style=flat-square&logo=android">
  <img src="https://img.shields.io/badge/targetSdk-31-%233DDC84?style=flat-square&logo=android">
</p>
ClosetCanvas is a simple closet management application written in Kotlin using the Jetpack Compose toolkit. The idea was born out of the need to create an app that was simple but provided a solid tool for managing personal wardrobes, while not inundating the user with thousands of ads or pushing products the user doesn't need.

## Key functionalities
The app provides the following key functionalities:
- Logging in to the application using your Google account
- Creating your own closet using the layout wizard
- Adding items to your closet, with the ability to add a photo, name, description, category and tags
- Ability to assign clothes a date when they were last washed, so that they stay fresh
- Displaying a list of all items, with the ability to filter by name, category and tags
- Creating your own collections of clothes for different occasions

## Known bugs
- [X] Users can add items without the fields required for the application to work, such as a photo of the item
- [ ] Users can create wardrobe without layout, which blocks their ability to add items
- [ ] Deleting the wardrobe leaves empty files in the Firestore, because there are nested collections associated with it

## User Interface
When creating the user interface, I wanted to keep it simple, so that the user would not get lost in thousands of options, but with a few gestures, do what they want to do.

### Login screen
<img height=500 src="https://i.ibb.co/0n1Pvwc/login-screen.png"> <img height=500 src="https://i.ibb.co/D1VgCYp/sign-with-google-no-email.png">

### Main screen
<img height=500 src="https://i.ibb.co/jHTzY5f/main-screen.png">

### Profile screen
<img height=500 src="https://i.ibb.co/SxB7Q8q/profile-screen.png">

### Wardrobe creator
<img height=500 src="https://i.ibb.co/Tmy2b5G/create-new-wardrobe.png"> <img height=500 src="https://i.ibb.co/172trDg/create-wardrobe-no-name.png">

### Managing wardrobe
<img height=500 src="https://i.ibb.co/x5gGKT6/manage-wardrobe-no-layout.png"> <img height=500 src="https://i.ibb.co/99fxHPm/manage-wardrobe-with-layout.png">

### Layout creator wizard
<img height=500 src="https://i.ibb.co/K0KQTDS/wardrobe-layout-creation.png">

### Viewing wardrobe layout
<img height=500 src="https://i.ibb.co/7tpxjSB/select-space.png">

### Adding new item
<img height=500 src="https://i.ibb.co/3cp571d/adding-new-item.png"> <img height=500 src="https://i.ibb.co/D9cv2gq/date-picker-new-item.png"> <img height=500 src="https://i.ibb.co/GP33fR1/add-new-item-filled.png">  <img height=500 src="https://i.ibb.co/Lz94kRH/add-new-item-no-photo.png">

### Viewing item details and deleting
<img height=500 src="https://i.ibb.co/dLQVp7D/item-details.png"> <img height=500 src="https://i.ibb.co/V9Z5cZX/delete-item-dialog.png">

### Editing item details
<img height=500 src="https://i.ibb.co/SJ8BxY5/edit-details.png"> <img height=500 src="https://i.ibb.co/LY68qJg/edit-details-no-description.png">

### Saving collections
<img height=500 src="https://i.ibb.co/Wy0FWr6/save-collection.png"> <img height=500 src="https://i.ibb.co/sKpXf0t/create-collection-no-name.png">

### Browsing and deleting collections
<img height=500 src="https://i.ibb.co/MhfT0DM/collections.png"> <img height=500 src="https://i.ibb.co/5BL1Sqx/delete-collection-dialog.png">

### Filtering items
<img height=500 src="https://i.ibb.co/vcw0kZG/filtering-options.png">

## License
> [!CAUTION]
> Since this is a project done as part of my engineering thesis, in its current state, it is forbidden to use the application code for commercial purposes, due to the policy of the Cracow University of Technology.
