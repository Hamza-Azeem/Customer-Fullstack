const UserProfile = ({name, age, gender, id, ...props}) => {
    const category = gender === "MALE" ? "men" : "women"
    return (
        <div>

            <p>{name}</p>
            <p>{age}</p>
            <img src={`https://randomuser.me/api/portraits/${category}/${id}.jpg`} />
            {props.children}
        </div>
    );
}

export default UserProfile;