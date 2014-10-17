package com.brogrammers.agora.test;

import junit.framework.TestCase;


public class FilterPictureTest extends TestCase
{
//contains both images and no image posts
Question<List> imagelist;
//contains no images posts
Question<List> plainlist;


// assuming enum 0 = IMAGE, 1 = NOIMAGE
Question image1 = new Question("How do I beat rain teams?", "They are too fast. Thunder is too accurate in the rain.", null);
Question image2 = new Question ("How do I beat sand teams?", "Tyranitar is a broken mon.", null );
Question image3 = new Question ("How do I beat sun teams?", "Sunflora is too good.", null );
Question noimage1 = new Question ("How many apricots do I need for a masterball", "I want one to catch a magikarp.", null );
Question noimage2 = new Question ("My pokemon won't listen to me.", "My friend traded me his lv100 Oddish and it won't listen to me. Please help!", null );
Question noimage3 = new Question ("What should I wear today?", "I heard shorts are comfy.", null );

//images
Image img1 = null;
try {
URL url = new URL("http://cdn.bulbagarden.net/upload/2/25/Bulbapedia_logo.png");
img1 = ImageIO.read(url);
} catch (IOException e) {
}
Image img2 = null;
try {
URL url = new URL("http://cdn.bulbagarden.net/upload/thumb/0/0d/025Pikachu.png/600px-025Pikachu.png");
img2 = ImageIO.read(url);
} catch (IOException e) {
}
Image img3 = null;
try {
URL url = new URL("http://cdn.bulbagarden.net/upload/thumb/9/98/192Sunflora.png/600px-192Sunflora.png");
img3 = ImageIO.read(url);
} catch (IOException e) {
}

image1.setImage(img2);
image2.setImage(img1);
image3.setImage(img3);

//filter by having an image
imagelist.add(image1);
imagelist.add(image2);
imagelist.add(noimage1);
imagelist.add(noimage2);
imagelist.add(noimage3);

imagelist.filterBy(1);

assertTrue("List has no images.", imagelist.size() == 2);

//filter by not having images

plainlist.add(image1);
plainlist.add(image2);
plainlist.add(image3);

plainlist.filterBy(0);

assertTrue("List not filtered by not having image.", plainlist.size() == 0);



}
