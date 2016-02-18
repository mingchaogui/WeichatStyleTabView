package com.mingchaogui.twiggle.util;


import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.request.ImageRequest;


public class FrescoUtil {

    public static FileBinaryResource getFileFromDiskCache(String uriString) {
        ImageRequest imageRequest = ImageRequest.fromUri(uriString);
        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(imageRequest);
        BinaryResource resource = ImagePipelineFactory.getInstance().getMainDiskStorageCache().getResource(cacheKey);

        return  resource == null ? null : (FileBinaryResource)resource;
    }
}
