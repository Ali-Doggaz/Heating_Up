import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faCog } from '@fortawesome/free-solid-svg-icons'
import earthImage from '../assets/images/earth-heating.jpg'
import Notifications from '../components/Notifications'

const top_news =[
    {
        image: earthImage,
        title:'forest on fire',
        location:'California, United states',
        description:'lorem dklqjfqdmlfj dlfjqlmfkjq sdfsdlfksd sdfqdfqd dsf dfqsd qdqdmfqkj'
    },
    {
        image: earthImage,
        title:'forest on fire',
        location:'California, United states',
        description:'lorem dklqjfqdmlfj dlfjqlmfkjq sdfsdlfksd sdfqdfqd dsf dfqsd qdqdmfqkj lorem dklqjfqdmlfj dlfjqlmfkjq sdfsdlfksd sdfqdfqd dsf dfqsd qdqdmfqkj lorem dklqjfqdmlfj dlfjqlmfkjq sdfsdlfksd sdfqdfqd dsf dfqsd qdqdmfqkj'
    },
    {
        image: earthImage,
        title:'forest on fire',
        location:'California, United states',
        description:'lorem dklqjfqdmlfj dlfjqlmfkjq sdfsdlfksd sdfqdfqd dsf dfqsd qdqdmfqkj'
    },
]

export default function HomePage() {

    const [showNav, setShowNav] = useState(false);

    useEffect(()=>{
        const news = document.querySelectorAll(".new");
        console.log(news)

        var options = {
            root: document.querySelector('#scrollArea'),
            rootMargin: '0px 0px -100px 0px',
            threshold: 1
        }
          
        var observer = new IntersectionObserver(function(entries,observer){
            entries.forEach(entry=>{
                if(!entry.isIntersecting){
                    return;
                }   
                entry.target.classList.add("visible");
            })
        }, options);

        news.forEach(neww=>{
            observer.observe(neww)
        })

        return ()=>{
            observer.disconnect();
        }
    },[])

    return (
        <div className="home">
            <div className="settings" >
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
            <Notifications/>
        </div>
    )
}
