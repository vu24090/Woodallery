import Header from "../components/layout/Header";
import Footer from "../components/layout/Footer";
import headBanner from "../assets/images/head-banner.png";
import collectionBanner from "../assets/images/collection-banner.png";
import tailBanner from "../assets/images/last-banner.jpg";

export default function HomePage() {
  return (
    <>
      <Header />

      <main>
        {/* HERO */}
        <section
          className="hero"
          style={{
            backgroundImage: `
      linear-gradient(90deg, rgba(0,0,0,0.65), rgba(0,0,0,0.15)),
      url(${headBanner})
    `,
            backgroundPosition: "center",
            backgroundSize: "cover",
          }}
        >
          <div className="hero-content">
            <span>NỘI THẤT GỖ CAO CẤP</span>

            <h1>
              Không gian sống
              <br />
              mang dấu ấn riêng
            </h1>

            <p>
              Những sản phẩm nội thất được chế tác từ gỗ tự nhiên, tối giản và
              bền vững.
            </p>

            <button>Xem sản phẩm</button>
          </div>
        </section>

        <section
          className="promo-banner-collection"
          style={{
            backgroundImage: `
      linear-gradient(90deg, rgba(0,0,0,0.65), rgba(0,0,0,0.15)),
      url(${collectionBanner})
    `,
            backgroundPosition: "center",
            backgroundSize: "cover",
          }}
        >
          <div className="promo-content-right">
            <span>BỘ SƯU TẬP TRỰC TUYẾN</span>
            <h2>Nội thất gia dụng thủ công, tinh tế và bền vững</h2>
            <p>
              Khám phá các sản phẩm được chế tác tỉ mỉ từ bàn tay người thợ,
              mang phong cách cổ điển pha hiện đại. Mỗi món đồ đều là một câu
              chuyện, một điểm nhấn cho không gian sống của bạn.
            </p>
            <p>
              Trải nghiệm trực tiếp những thiết kế thủ công từ gỗ tự nhiên, kết
              hợp tinh tế giữa nét cổ điển và sự hiện đại trong từng chi tiết.
              Một không gian ấm áp, gần gũi và đầy cảm hứng cho tổ ấm của bạn.
            </p>
          </div>
        </section>

        <section
          className="promo-banner"
          style={{
            backgroundImage: `
linear-gradient(90deg, rgba(0,0,0,0.65), rgba(0,0,0,0.15)),
url(${tailBanner})
`,
            backgroundPosition: "center",
            backgroundSize: "cover",
          }}
        >
          <div className="promo-content">
            <span>THIẾT KẾ THEO YÊU CẦU</span>

            <h2>Không tìm thấy sản phẩm phù hợp?</h2>

            <p>
              Chúng tôi nhận thiết kế và sản xuất nội thất theo kích thước và
              phong cách riêng của bạn.
            </p>

            <button>Liên hệ với chúng tôi</button>
          </div>
        </section>
      </main>

      <Footer />
    </>
  );
}
