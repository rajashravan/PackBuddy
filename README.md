## tl;dr
If you ever find yourself in a situation where you need to store a certain number of objects in a limited space (like a backpack or suitcase) but don't have enough space to store everything, PackBuddy is your **buddy**! More specifically, if you are travelling and all of your items were bought with different currencies, PackBuddy converts everything for you and tells you which souvenirs to keep, and which to sadly throw away at the airport. But hey, maybe next time you'll learn not to keep that 3kg rock you found in Peru because it was _ lucky _.

## Inspiration
Being two people that have travelled a decent amount (with girlfriends who travel a lot more), we are no stranger to trying to travel with more stuff than we can bring. In addition, we both took a Combinatorics and Optimization class together and were fascinated by one particular problem, the Knapsack Problem.

PackBuddy solves the aforementioned problem by implementing a cool optimization algorithm.

## What it does
The Knapsack Problem is a problem in Combinatorial Optimization, where we have a certain number of objects, each with their own value and weight. We can't fit everything in our knapsack, so how do we pick our objects to maximize the amount of value of our knapsack?

Now imagine another layer of complexity - all the values of the objects are in different currencies (which would be very likely if you are travelling).

PackBuddy is able to take in all this information (name of the object, weight and value in any currency) and gives you the optimal packing arrangement to maximize your value. PackBuddy solves this through currency conversion and a general optimization algorithm.

## How we built it
Seeing as this use case would best be suited for a mobile app, we built PackBuddy as an Android App. Currency Conversion was implemented using XE's Currency Data API and we used a general purpose optimization algorithm to solve the Knapsack problem, with a few of our own modifications.

## Challenges we ran into
Learning how to use Web Requests in Android turned out to be a highly challenging ordeal that took us by surprise.
Ran into many dependency issues with Android, including some that led us starving over with a different Android OS version.
The Knapsack algorithm we found only accommodated integers, not doubles (like for prices), and started indexing arrays at 1 instead of 0 for some wacky reason. Modifying this algorithm to better suit our purposes took some effort.
We initially tried to integrate an image recognition API that would allow users to take pictures of receipts, but ended up taking too much time and was scrapped.

## Accomplishments that we're proud of
Getting it done! Honestly, there were times when we really stressed out and ran into challenges and weren't sure if we could even get a basic MVP done. Luckily, we pushed through and while it isn't pretty - it works! Perseverance definitely paid off.

## What we learned
iOS > Android. Nah jk, but we learned that theres a lot of smaller issues that can be hard to catch when developing on Android, but that a careful debugging session can quickly resolve these (some Stack Overflow helps too). We also learned how to use web requests with Android, how to make an OK looking Android and improved our dev skills overall.

## What's next for PackBuddy
