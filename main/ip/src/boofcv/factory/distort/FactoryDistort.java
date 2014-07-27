/*
 * Copyright (c) 2011-2014, Peter Abeles. All Rights Reserved.
 *
 * This file is part of BoofCV (http://boofcv.org).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package boofcv.factory.distort;

import boofcv.alg.distort.ImageDistort;
import boofcv.alg.distort.impl.*;
import boofcv.alg.interpolate.InterpolatePixelS;
import boofcv.core.image.border.ImageBorder;
import boofcv.struct.image.*;

/**
 * @author Peter Abeles
 */
public class FactoryDistort {

	/**
	 * Creates a {@link boofcv.alg.distort.ImageDistort} for the specified image type, transformation
	 * and interpolation instance.
	 *
	 * @param interp Which interpolation algorithm should be used.
	 * @param border Specifies how requests to pixels outside the image should be handled.  If null then no change
	 * @param outputType Type of output image.
	 */
	public static <Input extends ImageSingleBand, Ouput extends ImageSingleBand>
	ImageDistort<Input,Ouput> distort(InterpolatePixelS<Input> interp, ImageBorder border, Class<Ouput> outputType)
	{
		if( outputType == ImageFloat32.class ) {
			return (ImageDistort<Input,Ouput>)new ImplImageDistort_F32(interp,border);
		} else if( ImageSInt32.class.isAssignableFrom(outputType) ) {
			return (ImageDistort<Input,Ouput>)new ImplImageDistort_S32(interp,border);
		} else if( ImageInt16.class.isAssignableFrom(outputType) ) {
			return (ImageDistort<Input,Ouput>)new ImplImageDistort_I16(interp,border);
		} else if( ImageInt8.class.isAssignableFrom(outputType) ) {
			return (ImageDistort<Input,Ouput>)new ImplImageDistort_I8(interp,border);
		} else {
			throw new IllegalArgumentException("Output type not supported: "+outputType.getSimpleName());
		}
	}

	/**
	 * Creates a {@link boofcv.alg.distort.ImageDistort} for the multi-spectral images, transformation
	 * and interpolation instance.
	 *
	 * @param interp Which interpolation algorithm should be used.
	 * @param border Specifies how requests to pixels outside the image should be handled.  If null then no change
	 * @param outputType Type of output image.
	 */
	public static <Input extends ImageSingleBand,Output extends ImageSingleBand>
	ImageDistort<MultiSpectral<Input>,MultiSpectral<Output>>
	distortMS(InterpolatePixelS<Input> interp, ImageBorder border, Class<Output> outputType)
	{
		ImageDistort<Input, Output> distortSingle = distort(interp,border,outputType);
		return new ImplImageDistort_MS<Input, Output>(distortSingle);
	}

	/**
	 * Avoid recomputing the distortion map for the entire image each time
	 * by caching the distortion for each pixel.  This can improve speed significantly when the distortion
	 * and output image size are both constant.
	 *
	 * @param interp Which interpolation algorithm should be used.
	 * @param border Specifies how requests to pixels outside the image should be handled.  If null then no change
	 * @param outputType Type of output image.
	 * @return Image distort which caches the distortion.
	 */
	public static <Input extends ImageSingleBand,Output extends ImageSingleBand>
	ImageDistort<Input, Output> distortCached(InterpolatePixelS<Input> interp, ImageBorder border ,
											  Class<Output> outputType)
	{
		if( outputType == ImageFloat32.class ) {
			return (ImageDistort<Input,Output>)new ImplImageDistortCache_F32(interp,border);
		} else if( ImageSInt32.class.isAssignableFrom(outputType) ) {
			return (ImageDistort<Input,Output>)new ImplImageDistortCache_S32(interp,border);
		} else if( ImageInt16.class.isAssignableFrom(outputType) ) {
			return (ImageDistort<Input,Output>)new ImplImageDistortCache_I16(interp,border);
		} else if( ImageInt8.class.isAssignableFrom(outputType) ) {
			return (ImageDistort<Input,Output>)new ImplImageDistortCache_I8(interp,border);
		} else {
			throw new IllegalArgumentException("Output type not supported: "+outputType.getSimpleName());
		}
	}
}
