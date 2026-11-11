# Practice P2, quick start

## Files in this package

- `initial_solution.py`, the program to complete.
- `bikes.csv`, the data, 17,378 hours.

## Requirements

Python 3.10 or later, and two libraries:

    $ python3 --version
    $ pip install --user pandas scikit-learn

## Running

    $ python3 initial_solution.py

## Where the data comes from

The Bike Sharing dataset of the Capital Bikeshare service in Washington,
published by the University of Porto through the UCI Machine Learning
Repository. This file keeps the hours in their original order and the columns
the practice uses, and adds `next_hour`, the demand of the following hour, which
is the value to predict. The original is at
https://archive.ics.uci.edu/dataset/275/bike+sharing+dataset and needs no
account. This practice does not need it.

Attribution: Hadi Fanaee-T and Joao Gama. Event labeling combining ensemble
detectors and background knowledge. Progress in Artificial Intelligence, 2014.
Licensed under Creative Commons Attribution 4.0.
