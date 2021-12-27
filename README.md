# BeHero
![Be Hero](https://user-images.githubusercontent.com/24553205/147411995-79e56d9c-418c-4248-9903-d2652b4b4467.png)

## Latest android components are used to make this app 
    - MVVM Architecture
    - Navigation Graph
    - Single Activity Architecture
    - ViewBinding
    - Dagger Hilt
    - Firestore
    - Firebase Auth
    - Google Maps API
    - productFlavors
    - Glide
    - Coroutines
    - Custom Views
    - Base Class Structure (BaseActivity,BaseFragment,BaseAdapter)

---
## How the app will work?
 * This app keeps a list of voluntary donors.
 * A Voluntary Donor has to make an account on our app with some general information like:
    - Name
    - birthDay
    - Blood Group
    - Location
    - Gender
    - Phone Number
    
 * On the other side, the one who needs blood, will create a request for blood unit with his blood group and location.


# How to use
<ol>
  <li>  Clone the repository </li>
  <li>  Create firebase project and enabled to Sign-in providers by email and password </li>
  <li>  Create an Firestore DB with the following collections
    <ul>
      <li> users
        <ul>
          <li> name - String </li>         
          <li> bloodGroup - String </li>    
          <li> phone - String</li>         
          <li> mail - String</li>
          <li> mailVerified - Boolean</li>
          <li> birthDay - String</li>
          <li> createTime - Timestamp</li>
          <li> gender - String</li>
          <li> availableDonate - Boolean</li>
          <li> updateTime - Timestamp</li>
          <li> uuid - String</li>
          <li> address 
            <ul>
             <li> cityName - String</li>
             <li> countryCode - String</li>
             <li> countryName - String</li>
             <li> description - String</li>
             <li> latitude - Number</li>
             <li> longitude - Number</li>
             <li> subCityName - String</li>
            </ul>
          </li>
        </ul>
      </li>
      <li> donations
        <ul>
          <li> uuid - String</li>
          <li> patientName - String</li>
          <li> hospitalName - String</li>
          <li> description - String</li>
          <li> phone - String</li>
          <li> updateTime -Timestamp</li>
          <li> createTime -Timestamp</li>
          <li> enable - Boolean</li>
          <li> bloodGroup - String</li>
          <li> address 
            <ul>
             <li> cityName - String</li>
             <li> countryCode - String</li>
             <li> countryName - String</li>
             <li> description - String</li>
             <li> latitude - Number</li>
             <li> longitude - Number</li>
             <li> subCityName - String</li>
            </ul>
          </li>
        </ul>
      </li>
    </ul>
  </li>
  <li>  Import your google-services.json file into the android project </li>
  <li>  Get you own google map API key. See the quick guide to getting an API key: https://developers.google.com/maps/documentation/android-api/signup </li>
  <li> * Open the `gradle.properties` file and paste your API key into the value of the `GOOGLE_MAPS_API_KEY` property, like this

    `google_maps_key=PASTE-YOUR-API-KEY-HERE`
  </li>
</ol>

## Screenshots:
<img src="https://user-images.githubusercontent.com/24553205/147491427-61679e49-c09c-4703-ae8c-e9692699644c.jpg" width="200" /> &nbsp;&nbsp; 
<img src="https://user-images.githubusercontent.com/24553205/147491429-f87a8611-8d4d-4565-877a-4cfbb253b5cf.jpg" width="200" /> &nbsp;&nbsp;   
<img src="https://user-images.githubusercontent.com/24553205/147491431-e9a590dd-7fc5-408b-a7ff-eef036d94d24.jpg" width="200" /> &nbsp;&nbsp;
<img src="https://user-images.githubusercontent.com/24553205/147491432-7289efbe-d39c-47f5-8c3f-058f00c30c09.jpg" width="200" /> &nbsp;&nbsp; 
<img src="https://user-images.githubusercontent.com/24553205/147491433-67d89b3e-0a09-4319-86f8-d77b76c968db.jpg" width="200" /> &nbsp;&nbsp;
<img src="https://user-images.githubusercontent.com/24553205/147491416-76c0157d-fc83-41dd-9a65-10d19520bd45.jpg" width="200" /> &nbsp;&nbsp;
