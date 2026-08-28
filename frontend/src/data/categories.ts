export interface Category {
  id: string;
  name: string;
  slug: string;
  description: string;
  imageUrl: string;
}

export const categories: Category[] = [
  {
    id: "1",
    name: "Bàn",
    slug: "table",
    description: "Bàn ăn, bàn làm việc và bàn trang trí",
    imageUrl: "https://images.unsplash.com/photo-1533090481720-856c6e3c1fdc",
  },
  {
    id: "2",
    name: "Ghế",
    slug: "chair",
    description: "Ghế ăn, ghế thư giãn và ghế làm việc",
    imageUrl: "https://images.unsplash.com/photo-1503602642458-232111445657",
  },
  {
    id: "3",
    name: "Giường",
    slug: "bed",
    description: "Giường gỗ cho không gian phòng ngủ",
    imageUrl: "https://images.unsplash.com/photo-1505693416388-ac5ce068fe85",
  },
  {
    id: "4",
    name: "Tủ",
    slug: "cabinet",
    description: "Tủ lưu trữ với thiết kế tối giản",
    imageUrl: "https://images.unsplash.com/photo-1595428774223-ef52624120d2",
  },
];
