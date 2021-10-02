import React,{useState,createContext, useContext} from 'react'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faBell,faChevronLeft,faExclamationTriangle } from '@fortawesome/free-solid-svg-icons'


const notifContext = createContext();

const problem_table = [
    {
        title:"This forest is on fire",
        location: "San Francisco" ,
        description: "lorem dlskfjslfjlqkfjqmlkd jfkd lqjfdslfjqmlfdkj "
    },
    {
        title:"This forest is on fire",
        location: "San Francisco" ,
        description: "lorem dlskfjslfjlqkfjqmlkd jfkd lqjfdslfjqmlfdkj "
    },
    {
        title:"This forest is on fire",
        location: "San Francisco" ,
        description: "lorem dlskfjslfjlqkfjqmlkd jfkd lqjfdslfjqmlfdkj "
    },
    {
        title:"This forest is on fire",
        location: "San Francisco" ,
        description: "lorem dlskfjslfjlqkfjqmlkd jfkd lqjfdslfjqmlfdkj "
    },
    {
        title:"This forest is on fire",
        location: "San Francisco" ,
        description: "lorem dlskfjslfjlqkfjqmlkd jfkd lqjfdslfjqmlfdkj "
    },
    {
        title:"This forest is on fire",
        location: "San Francisco" ,
        description: "lorem dlskfjslfjlqkfjqmlkd jfkd lqjfdslfjqmlfdkj "
    },

    {
        title:"This forest is on fire",
        location: "San Francisco" ,
        description: "lorem dlskfjslfjlqkfjqmlkd jfkd lqjfdslfjqmlfdkj "
    },
    {
        title:"This forest is on fire",
        location: "San Francisco" ,
        description: "lorem dlskfjslfjlqkfjqmlkd jfkd lqjfdslfjqmlfdkj "
    },
    {
        title:"This forest is on fire",
        location: "San Francisco" ,
        description: "lorem dlskfjslfjlqkfjqmlkd jfkd lqjfdslfjqmlfdkj "
    },
    {
        title:"This forest is on fire",
        location: "San Francisco" ,
        description: "lorem dlskfjslfjlqkfjqmlkd jfkd lqjfdslfjqmlfdkj "
    },
]

function Circle(){

    const {nbrNotifs,setNotifShown} = useContext(notifContext)

    return (
        <div className="circle" onClick={()=>{setNotifShown(prev=>!prev)}}>
            <FontAwesomeIcon icon={faBell}/>
            <div className="red-circle">
                <p>{nbrNotifs}</p>
            </div>
        </div>
    )
}

function Notif({problem}){

    const [shown, setShown] = useState(false)

    return (
        <>
            <div className="problem" onClick={()=>{setShown(!shown)}}>
                <h3> <FontAwesomeIcon icon={faExclamationTriangle} className="icon"/> {problem.title} </h3>
                <p> {problem.location} </p>
                <div className={"description "+ (shown?"visible":"")}>
                    {problem.description}
                </div>
                <FontAwesomeIcon icon={faChevronLeft} className={"arrow "+(shown?"bottom":"")}/>
            </div>
            <hr />
        </>
    )
}

function NotifsContainer(){

    const {notifShown,problem_table} = useContext(notifContext)

    return (
        <div className={"notifications "+(notifShown?"visible":"")}>
            {problem_table.map(problem=>(
                <Notif problem={problem}/>
            ))}
        </div>
    )
}

export default function Notifications() {
    const [nbrNotifs, setNbrNotifs] = useState(problem_table.length)
    const [notifShown, setNotifShown] = useState(true)

    const value ={
        notifShown , setNotifShown,
        nbrNotifs, setNbrNotifs,
        problem_table
    }

    return (
        <notifContext.Provider value={value}>
            <Circle/>
            <NotifsContainer/>
        </notifContext.Provider>
    )
}
