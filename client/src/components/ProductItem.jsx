import styled from "styled-components";

const Item = styled.div`
  margin: 0 auto;
  padding: 10px;
  font-size: 14px;

  .image_wrapper {
    overflow: hidden;
    margin-bottom: 6px;
  }

  .name {
    margin-bottom: 5px;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    line-height: 20px;
    overflow: hidden;
    text-overflow: ellipsis;
  }

  .price {
    margin-bottom: 5px;
  }

  img {
    width: 100%;
    transition: 1s;

    &:hover {
      transform: scale(1.05);
    }
  }
`;

function ProductItem({ element }) {
  const ref = `/product/${element.id}`;

  return (
    <Item>
      <a href={ref}>
        <div className="image_wrapper">
          <img src={element.image}></img>
        </div>

        <div>
          <h3 className="name">{element.name}</h3>

          <div className="price">{element.price.toLocaleString()}원</div>
        </div>
      </a>
    </Item>
  );
}

export default ProductItem;
