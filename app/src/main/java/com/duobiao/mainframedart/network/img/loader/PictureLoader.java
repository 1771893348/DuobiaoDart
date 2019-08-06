/**
 * ClassName : PictureLoader</br>
 * <p/>
 * <p>2013© e-future.com.cn 版权所有 翻版必究</p>
 * <p>未经允许不得使用</p>
 */
package com.duobiao.mainframedart.network.img.loader;

/**
 *
 */
public class PictureLoader {

//    // 图片尺寸(服务器只)
//    public static String SIZE_SMALL = "!s1";
//    public static String SIZE_200_AUTO = "!s2";
//    public static String SIZE_1000_AUTO = "!s3";
//    public static String SIZE_50_50 = "!s4";
//    public static String SIZE_200_200 = "!s5";
//    public static String SIZE_640_220 = "!d1";
//    public static String SIZE_600_849 = "!d2";
//    public static String SIZE_100_100 = "!l1";
//    /**
//     * 是否为3G网络
//     */
//    private boolean mb3GModel = false;
//    private static PictureLoader sPictureLoader;
//    private DisplayImageOptions mImageOptions = null;
//    /**
//     * 文件格式匹配
//     */
//    private Pattern mFileExtPattern;
//
//    /**
//     * 单实例
//     *
//     * @return
//     */
//    public static PictureLoader getInstance() {
//        if (sPictureLoader == null) {
//            sPictureLoader = new PictureLoader();
//        }
//        return sPictureLoader;
//    }
//
//    private PictureLoader() {
//    }
//
//    /**
//     * 初始化，应该在{@link Application#onCreate()}中调用
//     *
//     * @param context
//     */
//    public void init(Context context) {
//        initOptions(context);
//        mFileExtPattern = Pattern.compile("(\\.[^/]+$)");
//        /**
//         * 初始化图片加载task
//         */
//        DisplayMetrics displayMetrics = context.getResources()
//                .getDisplayMetrics();
//        int screenWidth = displayMetrics.widthPixels;
//        int screenHeight = displayMetrics.heightPixels;
//        int maxMemory = (int) Runtime.getRuntime().maxMemory();
//        int cacheSize = maxMemory / 8;
//        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
//                context).threadPoolSize(3)
//                .diskCacheExtraOptions(screenWidth * 2, screenHeight * 2, null)
//                .memoryCacheExtraOptions(screenWidth, screenHeight)
//                // 设置内存缓存最大大小占当前应用可用内存的百分比 默认为一个当前应用可用内存的1/8
//                // .memoryCacheSizePercentage(8)
//                // 设置内存缓存 默认为一个当前应用可用内存的1/8大小的LruMemoryCache
//                .memoryCache(new LruMemoryCache(cacheSize))
//                // 设置内存缓存的最大大小 默认为一个当前应用可用内存的1/8
//                // .memoryCacheSize(cacheSize)
//                // .diskCacheFileCount(100)
//                .defaultDisplayImageOptions(mImageOptions)
//                // 设置硬盘缓存
//                // 默认为StorageUtils.getCacheDirectory(getApplicationContext())
//                // 即/mnt/sdcard/android/data/包名/cache/
//                // .diskCache(new
//                // UnlimitedDiskCache(StorageUtils.getCacheDirectory(HeibaApplication.getInstance())))
//                .diskCacheSize(50 * 1024 * 1024).build();
//        ImageLoader.getInstance().init(config);
//    }
//
//    private void initOptions(Context context) {
//        // mDmOptions = new DisplayImageOptions.Builder()
//        // .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
//        // .cacheInMemory(true).bitmapConfig(Config.ARGB_8888)
//        // .cacheOnDisk(true).showImageOnLoading(R.drawable.default_img)
//        // .showImageForEmptyUri(R.drawable.default_img)
//        // .showImageOnLoading(R.drawable.default_img)
//        // .considerExifParams(true).build();
//        // mIconOptions = new DisplayImageOptions.Builder()
//        // .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
//        // .showImageOnLoading(R.drawable.user_nor_ico)
//        // .showImageForEmptyUri(R.drawable.user_nor_ico)
//        // .showImageOnLoading(R.drawable.user_nor_ico)
//        // .cacheInMemory(true).bitmapConfig(Config.ARGB_8888)
//        // .cacheOnDisk(true).considerExifParams(false).build();
//        mImageOptions = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.logo_ico)
//                .showImageForEmptyUri(R.drawable.logo_ico)
//                .showImageOnLoading(R.drawable.logo_ico)
//                .displayer(new FadeInBitmapDisplayer(300))
//                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
//                .cacheInMemory(true).bitmapConfig(Config.ARGB_8888)
//                .cacheOnDisk(true).considerExifParams(false).build();
//    }
//
//    /**
//     * 设置是否为3G网络
//     *
//     * @param is3g
//     */
//    public void set3G(boolean is3g) {
//        mb3GModel = is3g;
//    }
//
//    /**
//     * 带默认图加载
//     *
//     * @param uri
//     * @param imageView
//     */
//    public void  loadImImage(String uri, ImageView imageView) {
//        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.train_catalog_hot_recomment_default_img)
//                .showImageForEmptyUri(R.drawable.train_catalog_hot_recomment_default_img)
//                .showImageOnFail(R.drawable.train_catalog_hot_recomment_default_img).cacheInMemory(true)
//                .bitmapConfig(Config.ARGB_8888).considerExifParams(false)
//                .cacheOnDisk(true).build();
//        loadOrginalImage(uri, imageView, options);
//    }
//
//    /**
//     * 自带参数加载
//     *
//     * @param uri
//     * @param imageView
//     * @param res       加载失败的时候，展示的图片
//     */
//    public void loadImage(String uri, ImageView imageView, int res) {
//        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .showImageForEmptyUri(res).showImageOnFail(res).showImageOnLoading(res)
//                .cacheInMemory(true).bitmapConfig(Config.ARGB_8888)
//                .cacheOnDisk(true)/* .showImageOnLoading(R.drawable.loading) */
//                .build();
//        loadOrginalImage(uri, imageView, options);
//    }
//
//    /**
//     * 下载原图bitmap
//     *
//     * @param uri
//     */
//    public void downloadPic(String uri, ImageLoadingListener listener) {
//        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .cacheInMemory(true).bitmapConfig(Config.ARGB_4444)
//                .cacheOnDisk(true)/* .showImageOnLoading(R.drawable.loading) */
//                .build();
//        ImageLoader.getInstance().loadImage(uri, options, listener);
//    }
//
//    /**
//     * 下载原图bitmap
//     *
//     * @param uri
//     */
//    public Bitmap downloadPic(String uri) {
//        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .cacheInMemory(true).bitmapConfig(Config.ARGB_8888)
//                .cacheOnDisk(true)/* .showImageOnLoading(R.drawable.loading) */
//                .build();
//        return getBitmapFromUri(uri, options);
//    }
//
//    /**
//     * 大图查看
//     *
//     * @param uri
//     * @param imageView
//     */
//    public void loadLargePicture(String uri, final ImageView imageView) {
//        DisplayImageOptions mLargeImageOptions = new DisplayImageOptions.Builder()
//                .cacheInMemory(true)
//                .showImageForEmptyUri(R.drawable.pictures_no)
//                .showImageOnFail(R.drawable.pictures_no)
//                .showImageOnLoading(R.drawable.loading)
//                .displayer(new SimpleBitmapDisplayer())
//                .resetViewBeforeLoading(true).bitmapConfig(Config.RGB_565)
//                .cacheOnDisk(true).considerExifParams(false).build();
//        loadLargePicture(uri, imageView, mLargeImageOptions);
//    }
//
//    public void loadLargePicture(String uri, final ImageView imageView, int res) {
//        DisplayImageOptions mLargeImageOptions = new DisplayImageOptions.Builder()
//                .cacheInMemory(true).showImageForEmptyUri(res)
//                .showImageOnFail(res).showImageOnLoading(R.drawable.loading)
//                .displayer(new SimpleBitmapDisplayer())
//                .resetViewBeforeLoading(true).bitmapConfig(Config.RGB_565)
//                .cacheOnDisk(true).considerExifParams(false).build();
//        final ScaleType scaleType = imageView.getScaleType();
//        ImageLoader.getInstance().displayImage(uri, imageView,
//                mLargeImageOptions, new ImageLoadingListener() {
//                    @Override
//                    public void onLoadingStarted(String arg0, View arg1) {
//                        ((ImageView) arg1).setScaleType(ScaleType.CENTER);
//                    }
//
//                    @Override
//                    public void onLoadingFailed(String arg0, View arg1,
//                                                FailReason arg2) {
//                        ((ImageView) arg1).setScaleType(scaleType);
//                    }
//
//                    @Override
//                    public void onLoadingComplete(String arg0, View arg1,
//                                                  Bitmap arg2) {
//                        ((ImageView) arg1).setScaleType(scaleType);
//                    }
//
//                    @Override
//                    public void onLoadingCancelled(String arg0, View arg1) {
//                        if (arg1 != null) {
//                            ((ImageView) arg1).setScaleType(scaleType);
//                        }
//                    }
//                }, new ImageLoadingProgressListener() {
//
//                    @Override
//                    public void onProgressUpdate(String arg0, View arg1,
//                                                 int arg2, int arg3) {
//                        if (arg1 == null)
//                            return;
//                        Drawable drawable = ((ImageView) arg1).getDrawable();
//                        if (drawable instanceof LevelListDrawable) {
//                            drawable.setLevel((int) (arg2 * 5.0 / arg3));
//                        }
//                    }
//                });
//    }
//
//    /**
//     * 大图查看
//     *
//     * @param uri
//     * @param imageView
//     */
//    public void loadLargePicture(String uri, ImageView imageView,
//                                 DisplayImageOptions options) {
//        final ScaleType scaleType = imageView.getScaleType();
//        ImageLoader.getInstance().displayImage(uri, imageView, options,
//                new ImageLoadingListener() {
//                    @Override
//                    public void onLoadingStarted(String arg0, View arg1) {
//                        ((ImageView) arg1).setScaleType(ScaleType.CENTER);
//                    }
//
//                    @Override
//                    public void onLoadingFailed(String arg0, View arg1,
//                                                FailReason arg2) {
//                        ((ImageView) arg1).setScaleType(ScaleType.CENTER);
//                    }
//
//                    @Override
//                    public void onLoadingComplete(String arg0, View arg1,
//                                                  Bitmap arg2) {
//                        ((ImageView) arg1).setScaleType(scaleType);
//                    }
//
//                    @Override
//                    public void onLoadingCancelled(String arg0, View arg1) {
//                        if (arg1 != null) {
//                            ((ImageView) arg1).setScaleType(ScaleType.CENTER);
//                        }
//                    }
//                }, new ImageLoadingProgressListener() {
//
//                    @Override
//                    public void onProgressUpdate(String arg0, View arg1,
//                                                 int arg2, int arg3) {
//                        if (arg1 == null)
//                            return;
//                        Drawable drawable = ((ImageView) arg1).getDrawable();
//                        if (drawable instanceof LevelListDrawable) {
//                            drawable.setLevel((int) (arg2 * 5.0 / arg3));
//                        }
//                    }
//                });
//    }
//
//    /**
//     * @param uri
//     * @return
//     */
//    private boolean isOrginalExit(String uri) {
//        return false;
//    }
//
//    /**
//     * @param uri
//     * @param imageView
//     * @param options
//     */
//    private void loadOrginalImage(String uri, ImageView imageView,
//                                  DisplayImageOptions options) {
//        ImageLoader.getInstance().displayImage(uri, imageView, options);
//
//    }
//
//    /**
//     * 获取bitmap
//     *
//     * @param uri
//     * @param options
//     */
//    private Bitmap getBitmapFromUri(String uri, DisplayImageOptions options) {
//        return ImageLoader.getInstance().loadImageSync(uri, options);
//    }
//
//    /**
//     * 从内存卡中异步加载本地图片
//     *
//     * @param uri
//     * @param imageView
//     */
//    public void displayFromSDCard(String uri, ImageView imageView) {
//        // String imageUri = "file:///mnt/sdcard/image.png"; // from SD card
//        DisplayImageOptions imageOptions = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.train_catalog_hot_recomment_default_img)
//                .showImageForEmptyUri(R.drawable.train_catalog_hot_recomment_default_img)
//                .showImageOnFail(R.drawable.train_catalog_hot_recomment_default_img)
//                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
//                .cacheInMemory(true).bitmapConfig(Config.RGB_565)
//                .cacheOnDisk(false).considerExifParams(false).build();
//        ImageLoader.getInstance().displayImage(
//                uri.startsWith("file://") ? uri : "file://" + uri, imageView,
//                imageOptions);
//    }
//
//    /**
//     * 从assets文件夹中异步加载图片
//     *
//     * @param imageName 图片名称，带后缀的，例如：1.png
//     * @param imageView
//     */
//    public void dispalyFromAssets(String imageName, ImageView imageView) {
//        // String imageUri = "assets://image.png"; // from assets
//        DisplayImageOptions imageOptions = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.logo_ico)
//                .showImageForEmptyUri(R.drawable.logo_ico)
//                .showImageOnFail(R.drawable.logo_ico)
//                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
//                .cacheInMemory(true).bitmapConfig(Config.RGB_565)
//                .cacheOnDisk(false).considerExifParams(false).build();
//        ImageLoader.getInstance().displayImage(
//                imageName.startsWith("assets://") ? imageName : "assets://"
//                        + imageName, imageView, imageOptions);
//    }
//
//    /**
//     * 从drawable中异步加载本地图片
//     *
//     * @param imageId
//     * @param imageView
//     */
//    public void displayFromDrawable(int imageId, ImageView imageView) {
//        // String imageUri = "drawable://" + R.drawable.image; // from drawables
//        // (only images, non-9patch)
//        DisplayImageOptions imageOptions = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.logo_ico)
//                .showImageForEmptyUri(R.drawable.logo_ico)
//                .showImageOnFail(R.drawable.logo_ico)
//                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
//                .cacheInMemory(true).bitmapConfig(Config.RGB_565)
//                .cacheOnDisk(false).resetViewBeforeLoading(true)
//                .considerExifParams(false).build();
//        ImageLoader.getInstance().displayImage("drawable://" + imageId,
//                imageView, imageOptions);
//    }
//
//    /**
//     * 从内容提提供者中抓取图片
//     */
//    public void displayFromContent(String uri, ImageView imageView) {
//        // String imageUri = "content://media/external/audio/albumart/13"; //
//        // from content provider
//        DisplayImageOptions imageOptions = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.logo_ico)
//                .showImageForEmptyUri(R.drawable.logo_ico)
//                .showImageOnFail(R.drawable.logo_ico)
//                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
//                .cacheInMemory(true).bitmapConfig(Config.RGB_565)
//                .cacheOnDisk(false).considerExifParams(false).build();
//        ImageLoader.getInstance().displayImage(
//                uri.startsWith("content://") ? uri : "content://" + uri,
//                imageView, imageOptions);
//    }
}
