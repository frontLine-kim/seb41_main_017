import axios from "axios";
import { useEffect } from "react";
import { useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import BASE_URL from "../../constants/BASE_URL";
import BasicButton from "../BasicButton";
import {
  DetailContainer,
  DetailBar,
  ContentBox,
  Page,
} from "../../styles/OneOnOneStyle";

function OneOnOneDetail() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [data, setData] = useState("");
  const [open, setOpen] = useState(false);

  const handleClickBtn = (e) => {
    e.preventDefault();
    navigate(`/service/one-on-one`);
  };

  const fetchData = () => {
    axios
      .get(`${BASE_URL}/board/inquiry/${id}`, {
        headers: {
          "Content-Type": `application/json`,
          authorization: JSON.parse(localStorage.getItem("token"))
            .authorization,
        },
      })
      .then((res) => setData(res.data.data))
      .catch((err) => console.error(err));
  };

  useEffect(() => {
    fetchData();
  }, []);

  const handleDelete = (e) => {
    e.preventDefault();

    if (window.confirm("삭제하시겠습니까?")) {
      axios
        .delete(`${BASE_URL}/board/inquiry/${id}`, {
          headers: {
            "Content-Type": `application/json`,
            authorization: JSON.parse(localStorage.getItem("token"))
              .authorization,
          },
        })
        .catch((err) => {
          console.log(err);
        });

      navigate(`/service/one-on-one`);
      window.location.reload();
    }
  };

  return (
    <Page>
      <DetailContainer>
        <div className="content_container">
          <div className="detail_title">{data.title}</div>
          <button onClick={handleDelete}>삭제</button>
        </div>
        <DetailBar>
          <div className="writter">나</div>
          <div className="category">카테고리</div>
          <div className="time">23.01.24</div>
        </DetailBar>
        <ContentBox>{data.content}</ContentBox>
      </DetailContainer>
      <div className="btn_container" onClick={handleClickBtn}>
        <BasicButton p_width={"20"} p_height={"7"}>
          목록으로{" "}
        </BasicButton>{" "}
      </div>
    </Page>
  );
}

export default OneOnOneDetail;