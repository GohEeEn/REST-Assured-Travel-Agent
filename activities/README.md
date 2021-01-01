# Travel Activities Recommender

A feature that generates a list of activities available in a given location. In ideal, this feature will links the destination of booked flight, and generate relevant activities to do for the customer.

## About this module

This module is meant to be an acivity finder for users who have booked their flight, and generate a list of activities according to their flight destination. Here is the full design stages to provide the service :

| Step | TODO                               | Purpose + HOW TO                                                   | Progress           |
| ---- | ---------------------------------- | ------------------------------------------------------------------ | ------------------ |
| 1    | Get location query from the user   | Location queries (ie. city & country) are accepted through App UI  | Not-applicable     |
| 2    | Search for geocode of a location   | Finding the location through its geocode with Nominatim Search API | Testing edge cases |
| 3    | Use the geocode to find activities | Required parameter : Geo-coordinate^                               | Done               |

^ Reference [here]([reference](https://amadeus4dev.github.io/amadeus-java/reference/com/amadeus/shopping/Activities.html))

## Tools & Libraries

- IntelliJ IDEA Community Edition
- Maven
- Postman
- [JSON Simple Library](https://mvnrepository.com/artifact/com.googlecode.json-simple/json-simple/1.1.1)
- [Amadeus API](https://developers.amadeus.com)
- [Nominatim Search API](https://nominatim.org/release-docs/develop/api/Search/) (Free + Open-Source, No registration required)

### Notes

1. API Credentials

- On Java class _ActivitiesRecommenderService line 20-21_, the API credentials are not included due to [security practice](https://developers.amadeus.com/blog/best-practices-api-key-storage).
- Contact [me](mailto:vincentgoh1998@gmail.com) to get my API credential or using your own Amadeus Developer Credentials.
- The implementation here fetches those credentials from the machine ENVIRONMENT variables *AMADEUS_CLIENT_ID* and *AMADEUS_CLIENT_SECRET* with `System.getenv()`

2. API Response Examples

- An example of __Nominatim Search API response__ is attached (ie. _successful_location_search.json_), while it is basically in __JSON__ format in this documentation [link](https://nominatim.org/release-docs/develop/api/Output/)
- There is also an example of __Activty[] Response__ of __Amadeus Tours & Activities API__ in this module (ie. _successful_activity_list.json_). The `Activity` class in `core` module is almost the duplicate of `Activity` class of Amadeus Java SDK, while it is in _Java Bean_ format, and only important information are stored.

### Input Tests

Tests for `https://test.api.amadeus.com/v1/shopping/activities?latitude=&longitude=` :

| Test    | Input to test                                              | Example (latitude, longitude)        | Activities? |
| ------- | ---------------------------------------------------------- | ------------------------------------ | ----------- |
| &#9745; | Geolocation of the supported location                      | Barcelona(41.397158, 2.160873)       | Yes         |
| &#9745; | Geolocation of the supported location                      | London(51.5073219,-0.1276474)        | Yes         |
| &#9745; | Geolocation of location doesn't included in the link above | Manchester(53.4794892,-2.2451148)    | Yes         |
| &#9745; | Geolocation of location doesn't included in the link above | Madrid(40.4167047, -3.7035825)       | Yes         |
| &#9745; | Geolocation of location doesn't included in the link above | Kuala Lumpur(3.1516964, 101.6942371) | Yes         |
| &#9745; | Geolocation of location doesn't included in the link above | Malacca(2.2245111, 102.2614662)      | No          |
| &#9745; | Geolocation of location doesn't included in the link above | Beijing(39.9020803,116.7185213)      | No          |

It turns out _searching/representing location with their geolocation is the most efficient way_, while not recommended for the UX, unless include another API to convert location string to the correponding geo-coordinate (Done by using __Nominatim API__)

**Note** : GPS coordinates checked with this [checker](https://www.gps-coordinates.net)

~~Tests for `Location location = amadeus.referenceData.location(id).get();`~~

| Test    | locationId | Location? | subType | Name                           | IATA code | Description                     |
| ------- | ---------- | --------- | ------- | ------------------------------ | --------- | ------------------------------- |
| &#9745; | ALHR       | Yes       | Airport |                                |           |                                 |
| &#9745; | ABCN       | Yes       | Airport |                                | BCN       |                                 |
| &#9745; | CBCN       | Yes       | City    |                                | BCN       |                                 |
| &#9745; | CLON       | Yes       | City    |                                | LON       |                                 |
| &#9745; | ALON       | No        | -       |                                |           | No airport with IATA code `LON` |
| &#9745; | ALAS       | Yes       | Airport | LAS VEGAS/NV/US:MCCARRAN INTER | LAS       |                                 |
| &#9745; | CLAS       | Yes       | City    | LAS VEGAS/NV/US:MCCARRAN INTER | LAS       |                                 |
| &#9745; | AJFK       | Yes       | Airport | NEW YORK/NY/US:JOHN F KENNEDY  | JFK       |                                 |
| &#9745; | CJFK       | No        | -       |                                |           | No city with IATA code `JFK`    |
| &#9745; | ALGA       | Yes       | Airport | NEW YORK/NY/US:LAGUARDIA       | LAS       |                                 |
| &#9745; | CLGA       | No        | -       |                                |           | No city with IATA code `JFK`    |
| &#9745; | CBLR       | Yes       | City    | BENGALURU/KA/IN:KEMPEGOWDA INT | BLR       |                                 |
| &#9745; | ABLR       | Yes       | Airport | BENGALURU/KA/IN:KEMPEGOWDA INT | BLR       |                                 |
| &#9745; | ABER       | Yes       | Airport | BERLIN/DE:BRANDENBURG          | BER       |                                 |
| &#9745; | CBER       | Yes       | City    | BERLIN/DE:BRANDENBURG          | BER       |                                 |

~~The String parameter seems to be __subType_prefix + IATA_code__, however the response is uncertain (ie. not found even if the information is available) since the regex of the `locationId` is __unknown__~~ (__NOT USED__)

### References

- [Amadeus Java SDK](https://github.com/amadeus4dev/amadeus-java)
- [Amadeus API Java SDK Documentation](https://amadeus4dev.github.io/amadeus-java/reference/packages.html)
- [Amadeus Tours & Activities API Reference](https://developers.amadeus.com/self-service/category/destination-content/api-doc/tours-and-activities/api-reference)
- [Amadeus for Developers Documentation](https://documenter.getpostman.com/view/2672636/RWEcPfuJ?version=latest)
- [IATA Airline and Location Code Search](https://www.iata.org/en/publications/directories/code-search/)
- [GPS Coordinates Checker](https://www.gps-coordinates.net)
- [Nominatim : Open-Source search based on OpeenStreetMap data](https://nominatim.org/release-docs/develop/api/Search/)
- [Tutorial : How to Parse JSON Data From a REST API Using a Simple JSON Library](https://dzone.com/articles/how-to-parse-json-data-from-a-rest-api-using-simpl)
