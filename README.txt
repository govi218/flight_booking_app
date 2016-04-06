How to:
  
  - (1)Register: MainPage -> Press 'REGISTER' button -> Fill in all fields -> Press 'SUBMIT' button.
  - (2)Login: MainPage -> Fill in email/password fields -> Press 'LOGIN' button.

  Options when logged in as Client (Refer to 2):

  - (3)View Your Bookings: Open settings (Refer to 4) -> press 'My Bookings' button |OR| press the 'VIEW ALL BOOKINGS' button. 
  - (4)Open Settings: Press the settings button (Look for 3 horizontal lines on the header, to the left of the app name)
  - (5)Search Itineraries: Open settings (Refer to 4) -> press 'Search Itineraries' -> Fill in corresponding fields -> press 'SEARCH' |OR| press 'MAKE NEW BOOKING' 		button -> Fill in corresponding fields -> press 'SEARCH'.

  - (6)Booking Itineraries: Search itineraries (Refer to 5) -> press the itinerary that you want to book -> press 'BOOK ITINERARY'.
  - (7)View All Flights: Press 'VIEW ALL FLIGHTS'.
  - (8)View Flight info: View all flights (Refer to 7) -> press the flight which you want to view.
  - (9)View Your Bookings: Open settings (Refer to 4) -> press 'My Bookings button. |OR| press 'VIEW ALL BOOKINGS'.

  Options when logged in as Admin (Refer to 2):

  - (10)Change Password: Open Settings (Refer to 4) -> press 'Change Password' button -> Enter new password -> press 'OK'.
  - (11)Edit a clients information: press 'EDIT A CLIENT'S INFORMATION' button -> change desired fields -> press 'SUBMIT'.
  - (12)View and edit all flights: press 'VIEW AND EDIT ALL FLIGHTS' button -> press flight which you would like to view/edit -> change desired fields -> press 'SUBMIT'.

  *****************************************************************************************************************************************************
  ***NOTE: When Uploading a csv, the default path is '~/data/data/cs.bo7.p3/' so if you're uploading from a folder inside that path you must include it.
	I.E. 'flights/flight.csv' -> press 'UPLOAD' button. This would be in the path '~data/data/cs.bo7.p3/flights/flight.csv'. ***
  *****************************************************************************************************************************************************
 
  - (13)Upload clients from csv: press 'UPLOAD CLIENTS FROM CSV' button -> enter {filename/filepath} with extension '.csv' -> press 'UPLOAD'.
  - (14)Upload admins from csv: press 'UPLOAD ADMINS FROM CSV' button -> enter {filename/filepath} with extension '.csv' -> press 'UPLOAD'.
  - (15)Upload flights from csv: press 'UPLOAD FLIGHTS FROM CSV' button -> enter {filename/filepath} with extension '.csv' -> press 'UPLOAD'.

  Options when logged in as either Admin OR Client (Refer to 2):
  - (16)Logout: Open settings (Refer to 4) -> press 'Logout' button. |OR| Client/Admin main page and double tap the phone's built in back button.

BONUS FEATURES: Great user interface, Navigation Drawer, Haptic feedback, Partial Fragment Integration, Toasts messages and alert dialogs, Consistent colour scheme, 	   Clever search algorithm, double back press to logout, efficient serialization and deserialization methods.

NOTE ABOUT PASSWORD.TXT: We believe password.txt is a poor choice for storing all users passwords. Instead we chose to have each object store its on password because it 	would mean that all changes in passwords would require less resources to complete. We believe our design choice is superior to the passwords.txt way and therefore
	have not implemented it. To Login as an admin use credentials: a/a. To Login as client use credentials c/c.

