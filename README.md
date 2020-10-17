# Budget tracker

GraphQL API for storing transactions, categorising them, and calculating statistics to summarise them.

# TODOs

* Create an "ING export parser" that the transaction service can use.
* Order results by date.
* Pagination! Use dates for cursors.
* Switch to using Arrow library for error handling?
* Return more specific `errorType` values?
* Use union type in GraphQL API for categorised vs. un-categorised transactions?