package application;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.control.Slider;

public class CounterController implements Initializable {

    public Label test1;
    private Desktop desktop = Desktop.getDesktop();
    public TextArea sheepText;
    BufferedImage image = null;
    File file = null;
    BufferedImage result = null;
    int numberOfClusters = 0;
    int numberOfSingle = 0;

    @FXML
    ImageView imageView;
    @FXML
    Slider slider;
    @FXML
    Slider sliderN;
    @FXML
    ImageView imageView1;

    @FXML
    void openColourSepearteWindow(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ColourChannels.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();

            stage.setTitle("Colour Channels");
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (Exception e) {
            System.out.println("Can't load window, removed in previous version.");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File file = new File("src/Box13.jpg");
        imageView.setFitWidth(600);
        imageView.setFitHeight(600);
        try {
            setSliderN();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setSliderN() throws IOException {
        while (sliderN.isValueChanging()) {
            displayCounted();
        }
    }

    public void GetPixelColor() {

        BufferedImage img = null;
        File f = null;
        configureFileChooser(fileChooser);
        File file = fileChooser.showOpenDialog(null);

        try {
            f = new File(String.valueOf(file));
            img = ImageIO.read(f);
        } catch (IOException e) {
            System.out.println(e);
        }

// Two nested loops allow us to visit every spot in a 2D array.
// For every column I, visit every row J.

        //File file= new File("your_file.jpg");
        //BufferedImage image = ImageIO.read(file);
        int width = img.getWidth();
        int height = img.getHeight();
        int[][] pixelArray = new int[height][width];
        int counter = 0;

        // Getting pixel color by position x and y
        //int t = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
   /*               int clr = img.getRGB(x, y);
                    int red = (clr & 0x00ff0000) >> 16;
                    int green = (clr & 0x0000ff00) >> 8;
                    int blue = clr & 0x000000ff;*/
                counter++;

                System.out.print("arr[" + y + "][" + x + "] = " + counter + " ");
/*                    if(red > 200 && green > 200 && blue > 200) {
                        t++;
                        System.out.println(t);
                        //System.out.println("Red Color value = " + red);
                        //System.out.println("Green Color value = " + green);/*        try {
                        //            f = new File(String.valueOf(file));
                        //            img = ImageIO.read(f);
                        //        } catch (IOException e) {
                        //            System.out.println(e);
                        //        }*/
                //System.out.println("Blue Color value = " + blue);

            }

        }
        System.out.println(pixelArray.length + "t1");
        System.out.println(pixelArray[0].length + "t2");


    }

    public void blackandwhiteGraphics(final ActionEvent t) {

        try {
            boolean ingore2 = true;
            BufferedImage result = new BufferedImage(
                    image.getWidth(),
                    image.getHeight(),
                    BufferedImage.TYPE_BYTE_BINARY);

            Graphics2D graphic = result.createGraphics();
            graphic.drawImage(image, 0, 0, Color.BLACK, null);
            graphic.dispose();

            File output = new File("C:\\Users\\Owner\\Pictures\\sheep99.jpg");
            ImageIO.write(result, "jpg", output);

            Image imageC = SwingFXUtils.toFXImage(result, null);
            imageView.setImage(imageC);

        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("You must load an image first.");
        }
    }

    public void blackandwhite() {

        try {
            //image = result;
            result = ImageIO.read(file);
            int width = result.getWidth();
            int height = result.getHeight();

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {

                    //get pixel value
                    int p = result.getRGB(x, y);
                    int a = (p >> 24) & 0xff;
                    int r = (p >> 16) & 0xff;
                    int g = (p >> 8) & 0xff;
                    int b = p & 0xff;
                    int avg = (r + b + g) / 3;

                    if (avg < 130) {
                        avg = 0;
                    } else {
                        avg = 255;
                    }

                    p = (a << 24) | (avg << 16) | (avg << 8) | avg;
                    result.setRGB(x, y, p);

                    imageView.setFitWidth(600);
                    imageView.setFitHeight(600);

                }
            }


            File output = new File("C:\\Users\\Owner\\Pictures\\bwG.jpg");
            ImageIO.write(result, "jpg", output);

            Image imageC = SwingFXUtils.toFXImage(result, null);
            imageView.setImage(imageC);
        } catch (Exception e) {
            //e.printStackTrace();
            sheepText.setText("Please open an image first.");
        }
    }

    public void revertOriginal(final ActionEvent t) {
        try {
            Image revert = SwingFXUtils.toFXImage(image, null);
            imageView.setImage(revert);
        }
        catch (Exception e) {
            sheepText.setText("Please open an image first.");
        }
    }

    void viewImage(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ImgViewer.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage2 = new Stage();

            stage2.setTitle("Image Viewer");
            stage2.setScene(new Scene(root1));
            stage2.show();
        } catch (Exception e) {
            System.out.println("Can't load window");
        }
    }

    public void exitMenu(ActionEvent event) {
        System.exit(0);
    }

    final FileChooser fileChooser = new FileChooser();

    public void openSingleFile(final ActionEvent e) {

        try {
            configureFileChooser(fileChooser);
            file = fileChooser.showOpenDialog(null);

            File input = new File(String.valueOf(file));
            image = ImageIO.read(input);
            fileChooser.setTitle("Select a new image to load");

            imageView.setFitHeight(image.getHeight());
            imageView.setFitWidth(image.getWidth());

    /*        Graphics2D g2d = image.createGraphics();
            g2d.setColor(Color.RED);
            g2d.drawRect(0, 0, 100, 100);
            g2d.dispose();*/

            Image imageC = SwingFXUtils.toFXImage(image, null);
            imageView.setImage(imageC);


            imageView.setFitWidth(600);
            imageView.setFitHeight(600);
            imageView.setPreserveRatio(true);

        } catch (IOException e1) {
            //e1.printStackTrace();
            System.out.println("File Chooser Closed.");
        }
    }

    private static void configureFileChooser(final FileChooser fileChooser) {
        fileChooser.setTitle("Load an Image to convert and count sheep");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
    }

    public String openFile(File file) {
        String filePath = null;
        try {
            File file1 = new File(String.valueOf(file));
            Image image = new Image(file1.toURI().toString());
            imageView.setImage(image);
            desktop.open(file);


        } catch (IOException ex) {
            Logger.getLogger(
                    CounterController.class.getName()).log(
                    Level.SEVERE, null, ex
            );
        }
        return filePath;
    }

    public void displayCounted() {
        try {
            int width = result.getWidth();
            int height = result.getHeight();
            int[][] pixelArray = new int[height][width];

            sheepText.setText("Total sheep/clusters in image: " +
                    countSheep(pixelArray));
            System.out.println("Done.");
          //"\n" + "Number of sheep clusters: " + numberOfClusters + "\n" + "Number of individual sheep: " + numberOfSingle)
        }
        catch(Exception e){
            sheepText.setText("Please open an image first.");
        }
    }

    // Returns number of sheep in a[][]
    int countSheep(int pixelArray[][]) {
        blackandwhite();
        int height = pixelArray.length;
        int width = pixelArray[0].length;


        DisjointUnionSets dus = new DisjointUnionSets(height * width);
        DisjointUnionSets dus2 = new DisjointUnionSets(height * width);


        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // If cell is 0, nothing to do
                int clr = result.getRGB(x, y);
                AtomicInteger red = new AtomicInteger((clr & 0x00ff0000) >> 16);
                AtomicInteger green = new AtomicInteger((clr & 0x0000ff00) >> 8);
                AtomicInteger blue = new AtomicInteger(clr & 0x000000ff);


                if (red.get() == 0 && green.get() == 0 && blue.get() == 0) {
                    /*System.out.println("test20");*/
                    continue;
                    //System.out.println("test21");
                }
                int ignore2;

/*                int finalClr = clr;
                Runnable r1 = () -> {
                    red.set((finalClr & 0x00ff0000) >> 16);
                    green.set((finalClr & 0x0000ff00) >> 8);
                    blue.set(finalClr & 0x000000ff);
                };*/
                //Thread thread1 = new Thread(r1);

                //if (y + 1 < height) clr = img.getRGB(x, y + 1);
                //thread1.start();


                if (y + 1 < height && red.get() == 255 && green.get() == 255 && blue.get() == 255)
                    dus.union(y * (width) + x, (y + 1) * (width) + x);
                if (y - 1 >= 0 && red.get() == 255 && green.get() == 255 && blue.get() == 255)
                    dus.union(y * (width) + x, (y - 1) * (width) + x);
                if (x + 1 < width && red.get() == 255 && green.get() == 255 && blue.get() == 255)
                    dus.union(y * (width) + x, (y) * (width) + x + 1);
                if (x - 1 >= 0 && red.get() == 255 && green.get() == 255 && blue.get() == 255)
                    dus.union(y * (width) + x, (y) * (width) + x - 1);
                if (y + 1 < height && x + 1 < width && red.get() == 255 && green.get() == 255 && blue.get() == 255)
                    dus.union(y * (width) + x, (y + 1) * (width) + x + 1);
                if (y + 1 < height && x - 1 >= 0 && red.get() == 255 && green.get() == 255 && blue.get() == 255)
                    dus.union(y * width + x, (y + 1) * (width) + x - 1);
                if (y - 1 >= 0 && x + 1 < width && red.get() == 255 && green.get() == 255 && blue.get() == 255)
                    dus.union(y * width + x, (y - 1) * width + x + 1);
                if (y - 1 >= 0 && x - 1 >= 0 && red.get() == 255 && green.get() == 255 && blue.get() == 255)
                    dus.union(y * width + x, (y - 1) * width + x - 1);
/*
                red = 255;
                green = 0;
                blue = 0;
                clr = img.getRGB(x, y);

                //set the pixel value
                clr = (red << 16) | (green << 8) | blue;
                img.setRGB(x, y, clr);*/

                int t = dus.find(y * width + x);
            }

        }


        // Array to note down frequency of each set
        int[] c = new int[height * width];
        int numberOfSheep = 0;
        numberOfClusters = 0;
        numberOfSingle = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int clr = result.getRGB(x, y);
                int red = (clr & 0x00ff0000) >> 16;
                int green = (clr & 0x0000ff00) >> 8;
                int blue = clr & 0x000000ff;

                if (red == 255 && green == 255 && blue == 255) {
                    int t = dus.find(y * width + x);

                    int p = image.getRGB(x, y);
                    int r = (clr & 0x00ff0000) >> 16;
                    int g = (clr & 0x0000ff00) >> 8;
                    int b = clr & 0x000000ff;


                    double noise = sliderN.getValue();
                    if (c[t] >= 0 && c[t] < noise) {


                        //if (red <20 && green <20 && blue <20) {
                        //a = 255;

                        r = 0;
                        g = 0;
                        b = 0;
                        p = result.getRGB(x, y);

                        //set the pixel value
                        p = (r << 16) | (g << 8) | b;
                        result.setRGB(x, y, p);
                        c[t]++;

                    } else {
                        //c[t]++;
                        b = 5;
                    }

                    double count = slider.getValue();
                    if (c[t] == count) {
                        numberOfSheep++;
                        c[t]++;
                    }
                    if (c[t] == count + 1000) {
                        numberOfClusters++;
                        c[t]++;
                        System.out.println(numberOfClusters);
                    }
                    if (c[t] == 0 && c[t] < count) {
                        numberOfSingle++;
                        c[t]++;
                    } else {
                        c[t]++;
                    }


                }
            }
        }
        imageViewLoad();
        return numberOfSheep;


    }

    public void imageViewLoad() {
        imageView.setFitHeight(result.getHeight());
        imageView.setFitWidth(result.getWidth());

        Image imageC = SwingFXUtils.toFXImage(result, null);
        imageView.setImage(imageC);


        imageView.setFitWidth(600);
        imageView.setFitHeight(600);
        imageView.setPreserveRatio(true);
    }

    class DisjointUnionSets {
        int[] rank, parent;
        int n;

        public DisjointUnionSets(int n) {
            rank = new int[n];
            parent = new int[n];
            this.n = n;
            makeSet();
        }

        void makeSet() {
            for (int i = 0; i < n; i++)
                parent[i] = i;
        }


        int find(int x) {
            if (parent[x] != x) {
                return find(parent[x]);
            }

            return x;
        }


        void union(int x, int y) {
            int xRoot = find(x);
            int yRoot = find(y);

            if (xRoot == yRoot)
                return;

            if (rank[xRoot] < rank[yRoot])
                parent[xRoot] = yRoot;

            else if (rank[yRoot] < rank[xRoot])
                parent[yRoot] = xRoot;

            else {
                parent[yRoot] = xRoot;

                rank[xRoot] = rank[xRoot] + 1;
            }
        }
    }
}