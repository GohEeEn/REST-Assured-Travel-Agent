# Travel Activities Recommender

A feature that generates a list of activities available in a given location. In ideal, this feature will links the destination of booked flight, and generate relevant activities to do for the customer.

## Tools & Libraries

- IntelliJ IDEA Community Edition
- Maven
- Postman
- [Amadeus API](https://developers.amadeus.com)

## Notes

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

**Note** : GPS coordinates checked with this [checker](https://www.gps-coordinates.net)

### References

- [Amadeus Java SDK](https://github.com/amadeus4dev/amadeus-java)
- [Amadeus API Java SDK Documentation](https://amadeus4dev.github.io/amadeus-java/reference/packages.html)
- [Amadeus Tours & Activities API Reference](https://developers.amadeus.com/self-service/category/destination-content/api-doc/tours-and-activities/api-reference)
- [Amadeus for Developers Documentation](https://documenter.getpostman.com/view/2672636/RWEcPfuJ?version=latest)
- [Airport Code Database](https://www.airportcodedb.com)
- [GPS Coordinates Checker](https://www.gps-coordinates.net)
