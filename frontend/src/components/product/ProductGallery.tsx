// import { useState } from "react";
// import type { ProductImage } from "../../types/product";

// interface ProductGalleryProps {
//   images: ProductImage[];
//   productName: string;
// }

// export default function ProductGallery({
//   images,
//   productName,
// }: ProductGalleryProps) {
//   const [selectedImage, setSelectedImage] = useState(images[0]);

//   if (!images.length) {
//     return <div className="gallery-empty">Không có hình ảnh</div>;
//   }

//   return (
//     <div className="product-gallery">
//       <div className="gallery-main">
//         <img src={selectedImage.url} alt={productName} />
//       </div>

//       <div className="gallery-thumbnails">
//         {images.map((image) => (
//           <button
//             key={image.order}
//             className={
//               selectedImage.order === image.order
//                 ? "thumbnail active"
//                 : "thumbnail"
//             }
//             onClick={() => setSelectedImage(image)}
//           >
//             <img src={image.url} alt={`${productName} ${image.order}`} />
//           </button>
//         ))}
//       </div>
//     </div>
//   );
// }
import { useState } from "react";
interface ProductGalleryProps {
  imageUrl: string | null;
  productName: string;
}

export default function ProductGallery({
  imageUrl,
  productName,
}: ProductGalleryProps) {
  const [open, setOpen] = useState(false);
  if (!imageUrl) {
    return <div className="gallery-empty">Không có hình ảnh</div>;
  }

  return (
    <>
      {/* Ảnh chính */}
      <div className="product-gallery">
        <div className="gallery-main" onClick={() => setOpen(true)}>
          <img src={imageUrl} alt={productName} />
        </div>
      </div>

      {/* LIGHTBOX */}
      {open && (
        <div className="lightbox" onClick={() => setOpen(false)}>
          <div className="lightbox-content">
            <img src={imageUrl} alt={productName} />
          </div>
        </div>
      )}
    </>
  );
}
