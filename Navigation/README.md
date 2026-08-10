# Informal Settlement Rescue Navigation

JavaFX desktop application for route planning in informal or rural settlement images where normal GPS-based navigation may be limited.

The application allows a user to upload a settlement image, select a start point and destination point, and find a route using graph-based pathfinding. It also includes image comparison logic for comparing settlement images.

## Tech Stack

- Java
- JavaFX
- Eclipse
- Graph Abstract Data Type (ADT)
- Breadth-First Search (BFS)
- Image processing / binary image conversion

## Main Features

- Upload aerial or satellite-style settlement images.
- Convert image information into a graph structure.
- Represent locations using nodes and edges.
- Select start and destination points on the image.
- Find and draw a route using BFS pathfinding.
- Compare two settlement images using graph/image similarity logic.

## My Contribution

This was a group academic mini project. My contribution included defining graph abstract data types, working with nodes and edges, contributing to route logic and shortest path concepts, and supporting documentation/testing of the image-processing limitations.

## Important Classes

- `navigation.main.Main` - starts the JavaFX application.
- `navigation.ui.SettlementPane` - main user interface screen and button logic.
- `navigation.ui.SettlementCanvas` - draws the uploaded image, selected points, and route path.
- `navigation.utils.ImageToGraph` - converts image data into graph information.
- `navigation.algorithms.BFS` - performs pathfinding.
- `navigation.ui.CompareWindow` - compares two settlement images.
- `navigation.algorithms.SimilarityDetector` - calculates similarity between graph structures.

## Sample Images

Sample testing images are included in the `sample-images/` folder.

## How to Run

1. Open the project in Eclipse or another Java IDE.
2. Make sure JavaFX is installed and configured.
3. Add the JavaFX SDK `lib` folder to the build path.
4. Use VM arguments similar to:

```text
--module-path "C:\javafx-sdk-21.0.10\lib" --add-modules javafx.controls,javafx.fxml,javafx.swing
```

5. Run:

```text
navigation.main.Main
```

## Limitations

The image-processing approach depends on grayscale/binary conversion. Dark shadows, roofs, or unclear image areas may be misclassified as blocked paths. Some images may require preprocessing or manual adjustment for better results.

## Status

Completed group academic project.
