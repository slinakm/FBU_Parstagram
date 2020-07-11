# Project 4 - *Parstagram*

**Parstagram** is a photo sharing app using Parse as its backend.

Time spent: **16** hours spent in total

## User Stories

The following **required** functionality is completed:

- [x] User sees app icon in home screen.
- [x] User can sign up to create a new account using Parse authentication
- [x] User can log in and log out of his or her account
- [x] The current signed in user is persisted across app restarts
- [x] User can take a photo, add a caption, and post it to "Instagram"
- [x] User can view the last 20 posts submitted to "Instagram"
- [x] User can pull to refresh the last 20 posts submitted to "Instagram"
- [x] User can tap a post to view post details, including timestamp and caption.

The following **stretch** features are implemented:

- [x] Style the login page to look like the real Instagram login page.
- [x] Style the feed to look like the real Instagram feed.
- [x] User should switch between different tabs - viewing all posts (feed view), capture (camera and photo gallery view) and profile tabs (posts made) using fragments and a Bottom Navigation View.
- [x] User can load more posts once he or she reaches the bottom of the feed using endless scrolling.
- [x] Show the username and creation time for each post
- [x] After the user submits a new post, show an indeterminate progress bar while the post is being uploaded to Parse
- User Profiles:
  - [x] Allow the logged in user to add a profile photo
  - [x] Display the profile photo with each post
  - [x] Tapping on a post's username or profile photo goes to that user's profile page
  - [x] User Profile shows posts in a grid view
- [x] User can comment on a post and see all comments for each post in the post details screen.
- [x] User can like a post and see number of likes for each post in the post details screen.

The following **additional** features are implemented:

- [x] Implemented ViewModel for Camera fragment (which adds new posts) so that information is not lost from configuration changes.
- [x] Implemented a DialogFrament to show post details when user clicks on post image.

Please list two areas of the assignment you'd like to **discuss further with your peers** during the next class (examples include better ways to implement something, how to extend your app in certain ways, etc):

1. Set up a custom gallery so that we can select multiple images.
2. Set up notification so that users can be notified of new likes and comments.

## Video Walkthrough

Here's a walkthrough of implemented user stories: the last two are mp4 files because the gif files of the videos were too large. I also set up a Dropbox link with the password "codepath". 

<img src='https://github.com/slinakm/FBU_Parstagram/blob/master/gifs/parstagram_slightlylesstiny.gif' title='Video Walkthrough' width='' alt='https://github.com/slinakm/FBU_Parstagram/blob/master/gifs/parstagram_slightlylesstiny.gif' />

<img src='https://github.com/slinakm/FBU_Parstagram/blob/master/gifs/parstagram_persistence_200.gif' title='Video Walkthrough' width='' alt='https://github.com/slinakm/FBU_Parstagram/blob/master/gifs/parstagram_persistence_200.gif' />

<img src='https://www.dropbox.com/home?preview=parstagram_small.gif' title='Video Walkthrough' width='' alt='https://www.dropbox.com/home?preview=parstagram_small.gif' />



<img src='https://github.com/slinakm/FBU_Parstagram/blob/master/gifs/parstagram_full.mp4' title='Video Walkthrough' width='' alt='https://github.com/slinakm/FBU_Parstagram/blob/master/gifs/parstagram_full.mp4' />
<img src='https://github.com/slinakm/FBU_Parstagram/blob/master/gifs/parstagram_persistence.mp4' title='Video Walkthrough' width='' alt='https://github.com/slinakm/FBU_Parstagram/blob/master/gifs/parstagram_persistence.mp4' />

GIF created with [Kap](https://getkap.co/).

## Credits

List an 3rd party libraries, icons, graphics, or other assets you used in your app.

- [Android Async Http Client](http://loopj.com/android-async-http/) - networking library


## Notes

Describe any challenges encountered while building the app.
I started with a Bottom Navigation Bar template on IntelliJ, so learning how to deal with ModelViews and LiveData was very new to me, especially since the CodePath guide did not cover the MVVM architecture guide.

## License

    Copyright [2020] [Selina Kim]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
