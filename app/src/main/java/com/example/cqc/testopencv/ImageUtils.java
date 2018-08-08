package com.example.cqc.testopencv;


import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class ImageUtils {
    private static final int BLACK = 0;
    private static final int WHITE = 255;

    private Mat mat;


    public Mat getResult(){
        return  mat;
    }
    /**
     * 空参构造函数
     */
    public ImageUtils() {

    }

    /**
     * 通过图像路径创建一个mat矩阵
     *
     * @param imgFilePath
     *            图像路径
     */
    public ImageUtils(String imgFilePath) {
        mat = Imgcodecs.imread(imgFilePath);
    }

    public ImageUtils(Mat mat) {
        this.mat = mat;
    }

    /**
     * 加载图片
     *
     * @param imgFilePath
     */
    public void loadImg(String imgFilePath) {
        mat = Imgcodecs.imread(imgFilePath);
    }

    /**
     * 获取图片高度的函数
     *
     * @return
     */
    public int getHeight() {
        return mat.rows();
    }

    /**
     * 获取图片宽度的函数
     *
     * @return
     */
    public int getWidth() {
        return mat.cols();
    }

    /**
     * 获取图片像素点的函数
     *
     * @param y
     * @param x
     * @return
     */
    public int getPixel(int y, int x) {
        // 我们处理的是单通道灰度图
        return (int) mat.get(y, x)[0];
    }

    /**
     * 设置图片像素点的函数
     *
     * @param y
     * @param x
     * @param color
     */
    public void setPixel(int y, int x, int color) {
        // 我们处理的是单通道灰度图
        mat.put(y, x, color);
    }

    /**
     * 保存图片的函数
     *
     * @param filename
     * @return
     */
    public boolean saveImg(String filename) {
        return Imgcodecs.imwrite(filename, mat);
    }

    /**
     * 连通域降噪
     * @param pArea 默认值为1
     */
    public void contoursRemoveNoise(double pArea) {
        int i, j, color = 1;
        int nWidth = getWidth(), nHeight = getHeight();

        for (i = 0; i < nWidth; ++i) {
            for (j = 0; j < nHeight; ++j) {
                if (getPixel(j, i) == BLACK) {
                    //用不同颜色填充连接区域中的每个黑色点
                    //floodFill就是把一个点x的所有相邻的点都涂上x点的颜色，一直填充下去，直到这个区域内所有的点都被填充完为止
                    Imgproc.floodFill(mat, new Mat(), new Point(i, j), new Scalar(color));
                    color++;
                }
            }
        }

        //统计不同颜色点的个数
        int[] ColorCount = new int[255];

        for (i = 0; i < nWidth; ++i) {
            for (j = 0; j < nHeight; ++j) {
                if (getPixel(j, i) != 255) {
                    ColorCount[getPixel(j, i) - 1]++;
                }
            }
        }

        //去除噪点
        for (i = 0; i < nWidth; ++i) {
            for (j = 0; j < nHeight; ++j) {

                if (ColorCount[getPixel(j, i) - 1] <= pArea) {
                    setPixel(j, i, WHITE);
                }
            }
        }

        for (i = 0; i < nWidth; ++i) {
            for (j = 0; j < nHeight; ++j) {
                if (getPixel(j, i) < WHITE) {
                    setPixel(j, i, BLACK);
                }
            }
        }

    }


    /**
     * 8邻域降噪,又有点像9宫格降噪;即如果9宫格中心被异色包围，则同化
     * @param pNum 默认值为1
     */
    public void navieRemoveNoise(int pNum) {
        int i, j, m, n, nValue, nCount;
        int nWidth = getWidth(), nHeight = getHeight();

        // 对图像的边缘进行预处理
        for (i = 0; i < nWidth; ++i) {
            setPixel(i, 0, WHITE);
            setPixel(i, nHeight - 1, WHITE);
        }

        for (i = 0; i < nHeight; ++i) {
            setPixel(0, i, WHITE);
            setPixel(nWidth - 1, i, WHITE);
        }

        // 如果一个点的周围都是白色的，而它确是黑色的，删除它
        for (j = 1; j < nHeight - 1; ++j) {
            for (i = 1; i < nWidth - 1; ++i) {
                nValue = getPixel(j, i);
                if (nValue == 0) {
                    nCount = 0;
                    // 比较以(j ,i)为中心的9宫格，如果周围都是白色的，同化
                    for (m = j - 1; m <= j + 1; ++m) {
                        for (n = i - 1; n <= i + 1; ++n) {
                            if (getPixel(m, n) == 0) {
                                nCount++;
                            }
                        }
                    }
                    if (nCount <= pNum) {
                        // 周围黑色点的个数小于阀值pNum,把该点设置白色
                        setPixel(j, i, WHITE);
                    }
                } else {
                    nCount = 0;
                    // 比较以(j ,i)为中心的9宫格，如果周围都是黑色的，同化
                    for (m = j - 1; m <= j + 1; ++m) {
                        for (n = i - 1; n <= i + 1; ++n) {
                            if (getPixel(m, n) == 0) {
                                nCount++;
                            }
                        }
                    }
                    if (nCount >= 7) {
                        // 周围黑色点的个数大于等于7,把该点设置黑色;即周围都是黑色
                        setPixel(j, i, BLACK);
                    }
                }
            }
        }

    }
    //将灰度化的图片二值化
    public   void binaryzation() {
        int BLACK = 0;
        int WHITE = 255;
        int ucThre = 0, ucThre_new = 127;
        int nBack_count, nData_count;
        int nBack_sum, nData_sum;
        int nValue;
        int i, j;

        int width = mat.width(), height = mat.height();
        //寻找最佳的阙值
        while (ucThre != ucThre_new) {
            nBack_sum = nData_sum = 0;
            nBack_count = nData_count = 0;

            for (j = 0; j < height; ++j) {
                for (i = 0; i < width; i++) {
                    nValue = (int) mat.get(j, i)[0];

                    if (nValue > ucThre_new) {
                        nBack_sum += nValue;
                        nBack_count++;
                    } else {
                        nData_sum += nValue;
                        nData_count++;
                    }
                }
            }

            nBack_sum = nBack_sum / nBack_count;
            nData_sum = nData_sum / nData_count;
            ucThre = ucThre_new;
            ucThre_new = (nBack_sum + nData_sum) / 2;
        }

        //二值化处理
        int nBlack = 0;
        int nWhite = 0;
        for (j = 0; j < height; ++j) {
            for (i = 0; i < width; ++i) {
                nValue = (int) mat.get(j, i)[0];
                if (nValue > ucThre_new) {
                    mat.put(j, i, WHITE);
                    nWhite++;
                } else {
                    mat.put(j, i, BLACK);
                    nBlack++;
                }
            }
        }

        // 确保白底黑字
        if (nBlack > nWhite) {
            for (j = 0; j < height; ++j) {
                for (i = 0; i < width; ++i) {
                    nValue = (int) (mat.get(j, i)[0]);
                    if (nValue == 0) {
                        mat.put(j, i, WHITE);
                    } else {
                        mat.put(j, i, BLACK);
                    }
                }
            }
        }
    }
}