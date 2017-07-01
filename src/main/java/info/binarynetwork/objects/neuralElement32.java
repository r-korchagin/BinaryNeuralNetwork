package info.binarynetwork.objects;

public class neuralElement32 {
    private int level1; // count at 1 level
    private int level2; // count at 2 level
    private int level3; // count at 3 level
    private int level4; // count at 4 level

    private int[] link1_2;
    private int[] link2_3;
    private int[] link3_4;

    public int getLevel1() {
	return level1;
    }

    public void setLevel1(int level1) {
	this.level1 = level1;
    }

    public int getLevel2() {
	return level2;
    }

    public void setLevel2(int level2) {
	this.level2 = level2;
    }

    public int getLevel3() {
	return level3;
    }

    public void setLevel3(int level3) {
	this.level3 = level3;
    }

    public int getLevel4() {
	return level4;
    }

    public void setLevel4(int level4) {
	this.level4 = level4;
    }

    public int[] getLink1_2() {
	return link1_2;
    }

    public void setLink1_2(int[] link1_2) {
	this.link1_2 = link1_2;
    }

    public int[] getLink2_3() {
	return link2_3;
    }

    public void setLink2_3(int[] link2_3) {
	this.link2_3 = link2_3;
    }

    public int[] getLink3_4() {
	return link3_4;
    }

    public void setLink3_4(int[] link3_4) {
	this.link3_4 = link3_4;
    }

}