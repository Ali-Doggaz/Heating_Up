import React from 'react'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faCog } from '@fortawesome/free-solid-svg-icons'
import { Link } from 'react-router-dom'

export default function Search() {

    // const [topNews, setTopNews] = useState([
    //     {
    //         title:"hello",
    //         location:"location",
    //         description: "description"
    //     }
    // ])


    return (
        <div className="home">
            {/* <div className="settings" >
                <FontAwesomeIcon 
                    icon={faCog} 
                    className={"settings-icon "+(showNav?"rotate-180":"")}
                    onClick={()=>{setShowNav(!showNav)}}    
                />
                <Link to="/" className={showNav?"visible":""}>Change Settings</Link>
            </div>
            <div className="top-news container">
                <h3>TOP NEWS <Link to="/search">Search</Link></h3>
                
                <div className="news-container">
                    {
                        top_news.map((top_new,index)=>(
                            <div className={"new "+((index%2)?"inverse":"")} key={index}>
                                <img src={top_new.image} alt="" />
                                <div className="content">
                                    <h3> {top_new.title.toUpperCase()} </h3>
                                    <p className="location"> {top_new.location} </p>
                                    <p className="description"> {top_new.description} </p>
                                </div>
                            </div>
                        ))
                    }
                </div>
            </div>
            <Notifications/> */}
        </div>
    )
}
