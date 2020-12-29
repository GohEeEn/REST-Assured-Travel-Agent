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

- Due to my current API package is a _testing environment plan_, thus there is location support limitation to several cities over the world (_haven't fully tested yet_)
- Reference : <https://github.com/amadeus4dev/data-collection/blob/master/data/pois.md>

3. API Response Examples

- There are 2 examples of Amadeus Tours & Activities API responses in this module, ie. _successful_activity_list.json_ and _unexpected_activity_list.json_
- Both return successful responses, while the difference is that the _unexpected_activity_list.json_ contains activities which is unrelated to the given destination (ie. Bangalore, while activities in Dubai show up)
- Will make further research and update this documentation if there is new progress

### References

- [Amadeus Java SDK](https://github.com/amadeus4dev/amadeus-java)
- [Amadeus Tours & Activities API Reference](https://developers.amadeus.com/self-service/category/destination-content/api-doc/tours-and-activities/api-reference)
- [Amadeus for Developers Documentation](https://documenter.getpostman.com/view/2672636/RWEcPfuJ?version=latest)
