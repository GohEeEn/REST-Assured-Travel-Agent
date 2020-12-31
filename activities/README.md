# Travel Activities Recommender

A feature that generates a list of activities available in a given location. In ideal, this feature will links the destination of booked flight, and generate relevant activities to do for the customer.

## About this module

This module is meant to be an acivity finder for users who have booked their flight, and generate a list of activities according to their flight destination. Here is the design stages :

| Step | TODO                                               | Purpose + HOW TO                                                                                               | Progress          |
| ---- | -------------------------------------------------- | -------------------------------------------------------------------------------------------------------------- | ----------------- |
| 1    | Get useful destination info from the flight object | Link the flight booking from `flight` module to this module,                                                   |                   |
| 2    | Use the info above to get a location (skippable)   | Use the location as the center to search for activities, finding destination with Geo-coordinates or IATA Code | Testing Responses |
| 3    | Use the location above to find activities          | Required parameter : Geo-coordinate^                                                                           | Functioning       |

^ Reference [here]([reference](https://amadeus4dev.github.io/amadeus-java/reference/com/amadeus/shopping/Activities.html))

## Tools & Libraries

- IntelliJ IDEA Community Edition
- Maven
- Postman
- [Amadeus API](https://developers.amadeus.com)

### Notes

1. API Credentials

- On Java class _ActivitiesRecommenderService line 20-21_, the API credentials are not included due to [security practice](https://developers.amadeus.com/blog/best-practices-api-key-storage).
- Contact [me](mailto:vincentgoh1998@gmail.com) to get my API credential or using your own Amadeus Developer Credentials.
- The implementation here fetches those credentials from the machine ENVIRONMENT variables *AMADEUS_CLIENT_ID* and *AMADEUS_CLIENT_SECRET* with `System.getenv()`

2. Service Limitation on Destination

- ~~Due to my current API package is a _testing environment plan_, thus there is location support limitation to several cities over the world (_haven't fully tested yet_)~~
- After performing some tests with the geo-coordinate of several random cites (test cases can be found below), it turns out some of the cities excluded from the list on the following link are also supported, while not all of them (eg. Beijing, China and Malacca, Malaysia)
- Reference : <https://github.com/amadeus4dev/data-collection/blob/master/data/pois.md>

3. API Response Examples

- There are 2 examples of Amadeus Tours & Activities API responses in this module, ie. _successful_activity_list.json_ and _unexpected_activity_list.json_
- Both return successful responses, while the difference is that the _unexpected_activity_list.json_ contains activities which is unrelated to the given destination (ie. Bangalore, while activities in Dubai show up)
- Will make further research and update this documentation if there is new progress

### Input Tests

Tests for `https://test.api.amadeus.com/v1/shopping/activities/by-square?north=&west=&south=&east=` :

| Test    | Input to test                                              | Example (north, west, south, east)           | Activities? |
| ------- | ---------------------------------------------------------- | -------------------------------------------- | ----------- |
| &#9745; | Geolocation of the supported location                      | (52.541755, 13.354201, 52.490569, 13.457198) | Yes         |
| &#9745; | Geolocation of location doesn't included in the link above | (52, 13, 52, 13)                             | No          |

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

It turns out _searching/representing location with their geolocation is the most efficient way_, while not recommended for the UX, unless include another API to convert location string to the correponding geo-coordinate.

**Note** : GPS coordinates checked with this [checker](https://www.gps-coordinates.net)

Tests for `Location location = amadeus.referenceData.location(id).get();`

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

The String parameter seems to be __subType_prefix + IATA_code__

### References

- [Amadeus Java SDK](https://github.com/amadeus4dev/amadeus-java)
- [Amadeus API Java SDK Documentation](https://amadeus4dev.github.io/amadeus-java/reference/packages.html)
- [Amadeus Tours & Activities API Reference](https://developers.amadeus.com/self-service/category/destination-content/api-doc/tours-and-activities/api-reference)
- [Amadeus for Developers Documentation](https://documenter.getpostman.com/view/2672636/RWEcPfuJ?version=latest)
- [IATA Airline and Location Code Search](https://www.iata.org/en/publications/directories/code-search/)
- [GPS Coordinates Checker](https://www.gps-coordinates.net)
