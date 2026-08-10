# Informal Settlement Rescue Navigation

Informal Settlement Rescue Navigation is a JavaFX mini project designed to support route planning in informal settlement or rural environments where standard GPS navigation may be limited.

The system uses graph-based logic to represent locations as nodes and routes as weighted edges. It allows users to work with top-view settlement images, identify a start point and destination point, and calculate the shortest path between them.

## Project Type

This was a group academic mini project completed by a team of four students.

## Project Status

Completed mini project.

## Purpose

The purpose of this project was to explore how graph algorithms and basic image processing techniques can support navigation and route planning in areas with limited formal mapping infrastructure.

## Main Features

- Upload a top-view settlement or area image
- Convert images into grayscale or binary black-and-white format
- Represent locations using graph nodes
- Connect available paths using weighted edges
- Select a start point and destination point
- Calculate the shortest path between two points
- Compare settlement or route images using similarity logic

## My Contribution

My main contributions included:

- Contributing to the system design and project planning
- Defining the graph abstract data type, including nodes and edges
- Working on graph-based route logic and shortest path concepts
- Supporting image preprocessing using grayscale and binary conversion
- Assisting with testing, debugging, and documentation

## Tech Stack

- Java
- JavaFX
- Eclipse
- Graph ADT
- Shortest path logic
- Image preprocessing
- Similarity logic

## Limitations

The system was designed to work with simplified top-view images that are converted into grayscale or binary black-and-white format. In this approach, white areas are treated as walkable paths, while black areas are treated as blocked or non-walkable areas.

Because of this, the system may not work accurately with all satellite images. Some satellite images contain shadows, dark roofs, trees, or low-contrast areas that may be incorrectly interpreted as blocked paths after conversion to black and white.

This means the accuracy of the shortest path calculation depends on the quality and clarity of the input image. Images with strong shadows or unclear pathways may require preprocessing or manual adjustment before the route can be calculated correctly.

## Learning Focus

This project helped me strengthen my understanding of:

- Graph data structures
- Nodes and weighted edges
- Shortest path algorithms
- Java desktop application development
- Image preprocessing using grayscale and binary image conversion
- Image-based route planning concepts
- Limitations caused by shadows and low-contrast areas in satellite images
- Applying computer science theory to real-world problems

## Contributors
Author/profile maintained by: Vukosi Shikwambana
This project was completed as part of a four-member academic group project.

